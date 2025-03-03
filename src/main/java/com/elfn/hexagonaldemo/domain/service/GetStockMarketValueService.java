package com.elfn.hexagonaldemo.domain.service;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

/**
 * @Author: Elimane
 */
@Service
public class GetStockMarketValueService {

    public Mono<BigDecimal> getStockMarketValue(String symbol, Integer quantity) {
        return Mono.empty();
    }

}
