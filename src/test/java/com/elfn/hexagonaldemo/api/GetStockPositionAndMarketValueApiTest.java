package com.elfn.hexagonaldemo.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

/**
 * Classe de test pour l'API de récupération des positions et de la valeur de marché des actions.
 * Ce test utilise WebFluxTest pour tester un contrôleur Spring WebFlux de manière réactive.
 */
@WebFluxTest
public class GetStockPositionAndMarketValueApiTest {

    @Autowired
    private WebTestClient client;

    /**
     * Teste l'endpoint GET "/stock-position-market-value/{symbol}".
     * Vérifie que l'API répond avec un statut HTTP 200 (OK).
     */
    @Test
    void get() {

        String symbol = "aapl";

        client.get()
                .uri("/stock-position-market-value/" + symbol)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }
}
