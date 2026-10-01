package com.example.zwi.ingestion;

public interface Normalizer<S, T> {
    T normalize(S source);
}
