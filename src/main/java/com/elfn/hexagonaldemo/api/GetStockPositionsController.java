package com.elfn.hexagonaldemo.api;

import com.elfn.hexagonaldemo.dto.GetStockPositionAndMarketValueApiResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Elimane
 */

@RestController
public class GetStockPositionsController {

    @GetMapping("/stock-position-market-value/{symbol}")
    public GetStockPositionAndMarketValueApiResponseDto getStockPositionAndMarketValue(@PathVariable String symbol) {
        return new GetStockPositionAndMarketValueApiResponseDto(symbol, 0, "", 0.0, 0.0);
    }


}
