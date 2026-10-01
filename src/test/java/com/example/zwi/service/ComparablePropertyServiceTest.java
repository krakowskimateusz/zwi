package com.example.zwi.service;

import com.example.zwi.domain.MarketType;
import com.example.zwi.domain.Transaction;
import com.example.zwi.service.dto.ComparableAnalysisResult;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ComparablePropertyServiceTest {

    private final ComparablePropertyService service = new ComparablePropertyService(new TransactionStatisticsService());

    @Test
    void shouldCompareListingWithNearbyTransactions() {
        List<Transaction> transactions = List.of(
                tx("1", 52.21, 21.00, "780000", 52.4, 2, 2),
                tx("2", 52.22, 21.01, "745000", 48.1, 2, 2),
                tx("3", 52.30, 21.20, "930000", 61.2, 3, 6)
        );

        ComparableAnalysisResult result = service.compareListingToTransactions(
                52.21,
                21.00,
                52.0,
                2,
                2,
                new BigDecimal("849000"),
                12,
                2500,
                20,
                transactions
        );

        assertEquals(new BigDecimal("16326.92"), result.offerPricePerSqm());
        assertEquals(2, result.transactionPricePerSqmStats().count());
        assertTrue(result.deltaToMedianPct().compareTo(BigDecimal.ZERO) > 0);
    }

    private static Transaction tx(String id, double lat, double lon, String price, double area, int rooms, Integer floor) {
        return new Transaction(
                id,
                "prop-" + id,
                "RCN",
                LocalDate.now().minusMonths(1),
                new BigDecimal(price),
                area,
                rooms,
                floor,
                "Mokotów",
                lat,
                lon,
                MarketType.SECONDARY,
                "{}"
        );
    }
}
