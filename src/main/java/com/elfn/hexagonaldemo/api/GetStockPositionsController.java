package com.elfn.hexagonaldemo.api;

import com.elfn.hexagonaldemo.domain.model.StockPosition;
import com.elfn.hexagonaldemo.domain.service.GetStockMarketValueService;
import com.elfn.hexagonaldemo.domain.service.GetStockPositionService;
import com.elfn.hexagonaldemo.dto.GetStockPositionAndMarketValueApiResponseDto;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.security.Principal;

/**
 * @Author: Elimane
 */

@RestController
public class GetStockPositionsController {
    private final GetStockPositionService getStockPositionService;
    private final GetStockMarketValueService getStockMarketValueService;

    public GetStockPositionsController(GetStockPositionService getStockPositionService, GetStockMarketValueService getStockMarketValueService) {
        this.getStockPositionService = getStockPositionService;
        this.getStockMarketValueService = getStockMarketValueService;
    }

    @GetMapping("/stock-position-market-value/{symbol}")
    public Mono<GetStockPositionAndMarketValueApiResponseDto> getStockPositionAndMarketValue(@AuthenticationPrincipal Mono<Principal> principalMono, @PathVariable String symbol) {
        return principalMono.flatMap(principal ->
                        getStockPositionService.getStockPosition(principal.getName(), symbol))
                .zipWhen(stockPosition -> getStockMarketValueService.getStockMarketValue(symbol, stockPosition.getQuantity()), (stockPosition, marketValue) ->
                     new GetStockPositionAndMarketValueApiResponseDto(symbol, stockPosition.getQuantity(), stockPosition.getCurrencyCode(), stockPosition.getCost(), marketValue)
                );

    }
}
