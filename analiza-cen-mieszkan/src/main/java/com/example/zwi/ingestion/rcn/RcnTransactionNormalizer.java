package com.example.zwi.ingestion.rcn;

import com.example.zwi.domain.MarketType;
import com.example.zwi.domain.Transaction;
import com.example.zwi.ingestion.Normalizer;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

public class RcnTransactionNormalizer implements Normalizer<RcnRawRecord, Transaction> {
    @Override
    public Transaction normalize(RcnRawRecord source) {
        return new Transaction(
                "txn-" + source.externalId(),
                "prop-" + source.externalId(),
                "RCN",
                LocalDate.parse(source.transactionDate()),
                parseDecimal(source.price()),
                parseDouble(source.area()),
                parseInt(source.rooms(), 0),
                parseNullableInt(source.floor()),
                source.district(),
                parseDouble(source.latitude()),
                parseDouble(source.longitude()),
                parseMarketType(source.marketType()),
                source.rawLine()
        );
    }

    private static BigDecimal parseDecimal(String value) {
        return new BigDecimal(normalizeNumber(value));
    }

    private static double parseDouble(String value) {
        return Double.parseDouble(normalizeNumber(value));
    }

    private static int parseInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private static Integer parseNullableInt(String value) {
        try {
            return Integer.valueOf(value.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private static String normalizeNumber(String value) {
        return value.trim().replace(" ", "").replace(",", ".");
    }

    private static MarketType parseMarketType(String value) {
        if (value == null) {
            return MarketType.UNKNOWN;
        }
        String normalized = value.toLowerCase(Locale.ROOT);
        if (normalized.contains("pierw")) {
            return MarketType.PRIMARY;
        }
        if (normalized.contains("wtór") || normalized.contains("wtor")) {
            return MarketType.SECONDARY;
        }
        return MarketType.UNKNOWN;
    }
}
