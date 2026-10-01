package com.example.zwi.domain;

import java.math.BigDecimal;
import java.time.Instant;

public record Listing(
        String id,
        String propertyId,
        String source,
        String externalId,
        String url,
        OfferStatus status,
        BigDecimal price,
        Instant firstSeenAt,
        Instant lastSeenAt,
        String rawData
) {
}
