package com.elfn.hexagonaldemo.api;

import com.elfn.hexagonaldemo.domain.service.GetStockPositionService;
import com.elfn.hexagonaldemo.dto.GetStockPositionAndMarketValueApiResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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
    public Mono<GetStockPositionAndMarketValueApiResponseDto> getStockPositionAndMarketValue(@PathVariable String symbol) {
       return getStockPositionService.getStockPosition(symbol)
                .map(stockPosition ->  new GetStockPositionAndMarketValueApiResponseDto(stockPosition.getSymbol(), stockPosition.getQuantity().intValue(), stockPosition.getCurrencyCode(), stockPosition.getCost(), 0.0));
    }


}
