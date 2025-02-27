package com.elfn.hexagonaldemo.api;

import com.elfn.hexagonaldemo.domain.model.StockPosition;
import com.elfn.hexagonaldemo.domain.service.GetStockPositionService;
import com.elfn.hexagonaldemo.dto.GetStockPositionAndMarketValueApiResponseDto;
import com.github.javafaker.Faker;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

/**
 * Classe de test pour l'API de récupération des positions et de la valeur de marché des actions.
 * Ce test utilise WebFluxTest pour tester un contrôleur Spring WebFlux de manière réactive.
 */
@WebFluxTest(GetStockPositionsController.class)
@ExtendWith(MockitoExtension.class)
public class GetStockPositionAndMarketValueApiTest {

    @Autowired
    private WebTestClient client;


    @MockBean
    private GetStockPositionService getStockPositionService;

    private static Faker faker = Faker.instance();

    /**
     * Teste l'endpoint GET "/stock-position-market-value/{symbol}".
     * Vérifie que l'API répond avec un statut HTTP 200 (OK).
     */
    @Test
    void get() {
        // arrange
        String symbol = "aapl";
        StockPosition fakeStockPosition = getFakeStockPosition(symbol);

        when(getStockPositionService.getStockPosition(symbol)).thenReturn(Mono.just(fakeStockPosition));

        // act
        client.get()
                .uri("/stock-position-market-value/" + symbol)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                // assert
                .expectStatus().isOk()
                .expectBody(GetStockPositionAndMarketValueApiResponseDto.class)
                .value(dto -> assertAll(
                        () -> assertThat(dto.getSymbol()).isEqualTo(symbol),
                        () -> assertThat(dto.getQuantity().doubleValue()).isCloseTo(fakeStockPosition.getQuantity().doubleValue(), Offset.offset(1.0)),
                        () -> assertThat(dto.getCurrencyCode()).isEqualTo(fakeStockPosition.getCurrencyCode()),
                        () -> assertThat(dto.getCost().doubleValue()).isCloseTo(fakeStockPosition.getCost().doubleValue(), Offset.offset(0.0001))
                ));
    }

    private static StockPosition getFakeStockPosition(String symbol) {
       return new StockPosition(
                symbol,
                BigDecimal.valueOf(faker.number().randomDouble(2, 0, 100)),
                faker.currency().code(),
                BigDecimal.valueOf(faker.number().randomDouble(2, 0, 1000000))
        );
    }
}
