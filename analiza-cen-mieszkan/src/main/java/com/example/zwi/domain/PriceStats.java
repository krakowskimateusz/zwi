package com.example.zwi.domain;

import java.math.BigDecimal;

public record PriceStats(
        long count,
        BigDecimal min,
        BigDecimal max,
        BigDecimal average,
        BigDecimal median
) {
    public static PriceStats empty() {
        return new PriceStats(0, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
    }
}
