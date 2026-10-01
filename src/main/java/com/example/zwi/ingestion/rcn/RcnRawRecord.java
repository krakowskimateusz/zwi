package com.example.zwi.ingestion.rcn;

public record RcnRawRecord(
        String externalId,
        String transactionDate,
        String price,
        String area,
        String rooms,
        String floor,
        String district,
        String latitude,
        String longitude,
        String marketType,
        String rawLine
) {
}
