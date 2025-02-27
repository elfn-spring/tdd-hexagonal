package com.elfn.hexagonaldemo.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Elimane
 */

@Data
@AllArgsConstructor
public class StockPosition {
    private String symbol;
    private BigDecimal quantity;
    private String currencyCode;
    private BigDecimal cost;
}
