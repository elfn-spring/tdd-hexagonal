package com.elfn.hexagonaldemo.api;

import com.elfn.hexagonaldemo.domain.model.StockPosition;
import com.elfn.hexagonaldemo.domain.service.GetStockMarketValueService;
import com.elfn.hexagonaldemo.domain.service.GetStockPositionService;
import com.elfn.hexagonaldemo.dto.GetStockPositionAndMarketValueApiResponseDto;
import com.github.javafaker.Faker;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Classe de test pour l'API de récupération des positions et de la valeur de marché des actions.
 *
 * <p>Cette classe utilise **Spring WebFluxTest** pour tester un contrôleur Spring WebFlux de manière réactive.
 * Elle emploie **Mockito** pour simuler les services nécessaires et **WebTestClient** pour exécuter les tests sur l'API.</p>
 */
@WebFluxTest(GetStockPositionsController.class)
@ExtendWith(MockitoExtension.class)
public class GetStockPositionAndMarketValueApiTest {

    @Autowired
    private WebTestClient client;

    @MockBean
    private GetStockPositionService getStockPositionService;
    @MockBean
    private GetStockMarketValueService getStockMarketValueService;

    private static final Faker faker = Faker.instance();

    /**
     * Teste l'endpoint GET "/stock-position-market-value/{symbol}" avec un utilisateur authentifié.
     *
     * <p>Ce test vérifie que l'API retourne un statut **HTTP 200 (OK)** et une réponse JSON contenant :</p>
     * <ul>
     *   <li>Le symbole de l'action demandé</li>
     *   <li>La quantité d'actions possédées</li>
     *   <li>Le code de la devise utilisée</li>
     *   <li>Le coût total de la position</li>
     * </ul>
     *
     * <p>Le test utilise un utilisateur fictif **"elsior"** grâce à l'annotation `@WithMockUser`.</p>
     */
    @Test
    @WithMockUser(username = "elsior")
    void get() {
        // Arrange - Préparation des données de test
        String symbol = "aapl";
        StockPosition fakeStockPosition = getFakeStockPosition(symbol);
        BigDecimal fakeMarketPrice = BigDecimal.valueOf(faker.number().randomDouble(4, 100, 1000000));
        String user = "elsior";

        when(getStockPositionService.getStockPosition(user, symbol)).thenReturn(Mono.just(fakeStockPosition));
        when(getStockMarketValueService.getStockMarketValue(symbol, fakeStockPosition.getQuantity())).thenReturn(Mono.just(fakeMarketPrice));

        // Act - Exécution de la requête HTTP GET
        makeGetRequest(symbol)
                // Assert - Vérification des résultats
                .expectStatus().isOk()
                .expectBody(GetStockPositionAndMarketValueApiResponseDto.class)
                .value(dto -> assertAll(
                        () -> assertThat(dto.getSymbol()).isEqualTo(symbol),
                        () -> assertThat(dto.getQuantity().doubleValue())
                                .isCloseTo(fakeStockPosition.getQuantity().doubleValue(), Offset.offset(1.0)),
                        () -> assertThat(dto.getCurrencyCode()).isEqualTo(fakeStockPosition.getCurrencyCode()),
                        () -> assertThat(dto.getCost().doubleValue()).isCloseTo(fakeStockPosition.getCost().doubleValue(), Offset.offset(0.0001)),
                        () -> assertThat(dto.getMarketValue().doubleValue()).isCloseTo(fakeMarketPrice.doubleValue(), Offset.offset(0.1))
                ));
    }

    /**
     * Exécute une requête GET sur l'API pour récupérer la position d'une action.
     *
     * @param symbol Le symbole de l'action
     * @return La réponse de l'API sous forme de {@link WebTestClient.ResponseSpec}
     */
    private WebTestClient.ResponseSpec makeGetRequest(String symbol) {
        return client.get()
                .uri("/stock-position-market-value/" + symbol)
                .accept(MediaType.APPLICATION_JSON)
                .exchange();
    }

    /**
     * Teste le comportement de l'API lorsqu'un utilisateur n'a aucune position boursière enregistrée.
     *
     * Cas testé :
     * - L'utilisateur "peterpan" effectue une requête pour récupérer sa position boursière sur "AAPL".
     * - Le service `getStockPositionService` retourne un `Mono.empty()`, indiquant qu'aucune position n'est enregistrée.
     * - Le service `getStockMarketValueService` est appelé mais sa valeur ne devrait pas impacter la réponse.
     * - L'API doit répondre avec un statut HTTP 200 (OK) et un corps de réponse vide.
     */
    @Test
    @WithMockUser("elsior") // Simule un utilisateur authentifié nommé "peterpan"
    void emptyPosition() {
        String symbol = "aapl"; // Définition du symbole boursier testé

        // Simule l'absence de position boursière pour l'utilisateur "peterpan"
        when(getStockPositionService.getStockPosition("elsior", symbol)).thenReturn(Mono.empty());

        // Simule la récupération de la valeur du marché avec une valeur aléatoire
        when(getStockMarketValueService.getStockMarketValue(eq(symbol), any()))
                .thenReturn(Mono.just(BigDecimal.valueOf(faker.number().randomDouble(4, 100, 1000000))));

        // Effectue la requête GET et vérifie que la réponse est vide avec un statut 200 OK
        makeGetRequest(symbol)
                .expectStatus().isOk() // Vérifie que le statut HTTP est bien 200 OK
                .expectBody(Void.class); // Vérifie que le corps de la réponse est vide
    }


    /**
     * Teste l'accès à l'API avec un utilisateur anonyme.
     *
     * <p>Ce test vérifie que l'API retourne un statut **403 Forbidden**, indiquant que
     * l'utilisateur est reconnu mais n'a pas les autorisations nécessaires.</p>
     */
//    @Test
//    @WithAnonymousUser
//    void anonymousGet() {
//        getRequest("ooo")
//                .expectStatus()
//                .isForbidden();
//    }

    /**
     * Teste l'accès à l'API sans authentification.
     *
     * <p>Ce test vérifie que l'API retourne un statut **401 Unauthorized**, indiquant que
     * l'utilisateur doit être authentifié pour accéder à la ressource.</p>
     */
    @Test
    void unauthenticatedGet() {
        makeGetRequest("aapl")
                .expectStatus()
                .isUnauthorized();
    }

    /**
     * Génère une fausse position boursière pour les tests en utilisant la bibliothèque **JavaFaker**.
     *
     * @param symbol Le symbole de l'action
     * @return Une instance fictive de {@link StockPosition} avec des valeurs aléatoires
     */
    private static StockPosition getFakeStockPosition(String symbol) {
        return new StockPosition(
                symbol,
                faker.number().numberBetween(1, 100),
                faker.currency().code(),
                BigDecimal.valueOf(faker.number().randomDouble(2, 0, 1000000))
        );
    }
}
