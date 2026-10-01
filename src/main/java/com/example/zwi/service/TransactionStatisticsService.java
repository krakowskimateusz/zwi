package com.example.zwi.service;

import com.example.zwi.domain.PriceStats;
import com.example.zwi.domain.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TransactionStatisticsService {
    public PriceStats calculatePricePerSqmStats(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            return PriceStats.empty();
        }

        List<BigDecimal> values = transactions.stream().map(Transaction::pricePerSqm).sorted().toList();
        BigDecimal min = values.get(0);
        BigDecimal max = values.get(values.size() - 1);
        BigDecimal average = values.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(values.size()), 2, RoundingMode.HALF_UP);

        BigDecimal median;
        int middle = values.size() / 2;
        if (values.size() % 2 == 0) {
            median = values.get(middle - 1).add(values.get(middle)).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
        } else {
            median = values.get(middle);
        }

        return new PriceStats(values.size(), min, max, average, median);
    }

    public PriceStats calculatePriceStats(List<BigDecimal> prices) {
        if (prices.isEmpty()) {
            return PriceStats.empty();
        }
        List<BigDecimal> values = new ArrayList<>(prices);
        values.sort(Comparator.naturalOrder());
        BigDecimal min = values.get(0);
        BigDecimal max = values.get(values.size() - 1);
        BigDecimal average = values.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(values.size()), 2, RoundingMode.HALF_UP);
        BigDecimal median = values.size() % 2 == 0
                ? values.get(values.size() / 2 - 1).add(values.get(values.size() / 2)).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP)
                : values.get(values.size() / 2);
        return new PriceStats(values.size(), min, max, average, median);
    }
}
