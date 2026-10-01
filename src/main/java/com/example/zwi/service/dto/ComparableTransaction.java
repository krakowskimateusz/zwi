package com.example.zwi.service.dto;

import com.example.zwi.domain.Transaction;

public record ComparableTransaction(
        Transaction transaction,
        double distanceMeters,
        double similarityScore
) {
}
