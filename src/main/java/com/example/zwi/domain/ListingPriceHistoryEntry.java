package com.example.zwi.domain;

import java.math.BigDecimal;
import java.time.Instant;

public record ListingPriceHistoryEntry(
        String id,
        String listingId,
        BigDecimal price,
        Instant recordedAt
) {
}
