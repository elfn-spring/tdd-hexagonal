package com.elfn.hexagonaldemo.dto;

import lombok.*;

/**
 * @Author: Elimane
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetStockPositionAndMarketValueApiResponseDto {
    String symbol;
    Integer quantity;
    String currencyCode;
    Number cost;
    Number marketValue;
}
