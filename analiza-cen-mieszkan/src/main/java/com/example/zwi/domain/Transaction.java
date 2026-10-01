package com.example.zwi.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Transaction(
        String id,
        String propertyId,
        String source,
        LocalDate transactionDate,
        BigDecimal price,
        double area,
        int rooms,
        Integer floor,
        String district,
        double latitude,
        double longitude,
        MarketType marketType,
        String rawData
) {
    public BigDecimal pricePerSqm() {
        if (area <= 0d) {
            return BigDecimal.ZERO;
        }
        return price.divide(BigDecimal.valueOf(area), 2, java.math.RoundingMode.HALF_UP);
    }
}
