package com.elfn.hexagonaldemo.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Elimane
 */

@RestController
public class GetStockPositionsController {

    @GetMapping("/stock-position-market-value/{symbol}")
    public String getStockPositionAndMarketValue(@PathVariable String symbol) {
        return "Hello World";
    }


}
