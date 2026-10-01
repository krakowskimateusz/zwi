package com.example.zwi.service;

import com.example.zwi.domain.Transaction;
import com.example.zwi.domain.TransactionFilter;
import com.example.zwi.repository.TransactionRepository;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TransactionQueryService {
    private final TransactionRepository transactionRepository;

    public TransactionQueryService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> findByFilter(TransactionFilter filter) {
        return transactionRepository.findAll().stream()
                .filter(t -> filter.fromDate() == null || !t.transactionDate().isBefore(filter.fromDate()))
                .filter(t -> filter.toDate() == null || !t.transactionDate().isAfter(filter.toDate()))
                .filter(t -> filter.minPrice() == null || t.price().compareTo(filter.minPrice()) >= 0)
                .filter(t -> filter.maxPrice() == null || t.price().compareTo(filter.maxPrice()) <= 0)
                .filter(t -> filter.minPricePerSqm() == null || t.pricePerSqm().compareTo(filter.minPricePerSqm()) >= 0)
                .filter(t -> filter.maxPricePerSqm() == null || t.pricePerSqm().compareTo(filter.maxPricePerSqm()) <= 0)
                .filter(t -> filter.minArea() == null || t.area() >= filter.minArea())
                .filter(t -> filter.maxArea() == null || t.area() <= filter.maxArea())
                .filter(t -> filter.rooms() == null || t.rooms() == filter.rooms())
                .filter(t -> filter.district() == null || Objects.equals(t.district(), filter.district()))
                .collect(Collectors.toList());
    }
}
