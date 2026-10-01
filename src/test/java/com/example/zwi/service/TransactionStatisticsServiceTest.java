package com.example.zwi.service;

import com.example.zwi.domain.MarketType;
import com.example.zwi.domain.PriceStats;
import com.example.zwi.domain.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TransactionStatisticsServiceTest {

    private final TransactionStatisticsService service = new TransactionStatisticsService();

    @Test
    void shouldCalculateMedianAndAverage() {
        List<Transaction> transactions = List.of(
                tx("1", "500000", 50),
                tx("2", "600000", 50),
                tx("3", "700000", 50)
        );

        PriceStats stats = service.calculatePricePerSqmStats(transactions);

        assertEquals(3, stats.count());
        assertEquals(new BigDecimal("10000.00"), stats.min());
        assertEquals(new BigDecimal("14000.00"), stats.max());
        assertEquals(new BigDecimal("12000.00"), stats.average());
        assertEquals(new BigDecimal("12000.00"), stats.median());
    }

    private static Transaction tx(String id, String price, double area) {
        return new Transaction(
                id,
                "prop-" + id,
                "RCN",
                LocalDate.now(),
                new BigDecimal(price),
                area,
                2,
                3,
                "Mokotów",
                52.2,
                21.0,
                MarketType.SECONDARY,
                "{}"
        );
    }
}
