package com.elfn.hexagonaldemo.domain.service;

import com.elfn.hexagonaldemo.domain.model.StockPosition;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * @Author: Elimane
 */

@Service
public class GetStockPositionService {
    public Mono<StockPosition> getStockPosition(String user, String symbol) {
        return Mono.empty();
    }
}
