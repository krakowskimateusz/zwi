package com.example.zwi.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionFilter(
        LocalDate fromDate,
        LocalDate toDate,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        BigDecimal minPricePerSqm,
        BigDecimal maxPricePerSqm,
        Double minArea,
        Double maxArea,
        Integer rooms,
        String district
) {
}
