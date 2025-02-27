package com.elfn.hexagonaldemo.api;

import com.elfn.hexagonaldemo.domain.service.GetStockPositionService;
import com.elfn.hexagonaldemo.dto.GetStockPositionAndMarketValueApiResponseDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.security.Principal;

/**
 * @Author: Elimane
 */

@RestController
public class GetStockPositionsController {
    private final GetStockPositionService getStockPositionService;

    public GetStockPositionsController(GetStockPositionService getStockPositionService) {
        this.getStockPositionService = getStockPositionService;
    }

    @GetMapping("/stock-position-market-value/{symbol}")
    public Mono<GetStockPositionAndMarketValueApiResponseDto> getStockPositionAndMarketValue(@AuthenticationPrincipal Mono<Principal> principalMono, @PathVariable String symbol) {
        return principalMono.flatMap(principal ->
                        getStockPositionService.getStockPosition(principal.getName(), symbol))
                .map(stockPosition ->
                        new GetStockPositionAndMarketValueApiResponseDto(
                                symbol,
                                stockPosition.getQuantity(),
                                stockPosition.getCurrencyCode(),
                                stockPosition.getCost(),
                                // placeholders
                                0.0
            )
        );

    }
}
