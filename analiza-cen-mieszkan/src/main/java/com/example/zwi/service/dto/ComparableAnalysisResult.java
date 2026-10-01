package com.example.zwi.service.dto;

import com.example.zwi.domain.PriceStats;

import java.math.BigDecimal;
import java.util.List;

public record ComparableAnalysisResult(
        BigDecimal offerPricePerSqm,
        PriceStats transactionPricePerSqmStats,
        BigDecimal deltaToMedianPct,
        List<ComparableTransaction> comparables
) {
}
