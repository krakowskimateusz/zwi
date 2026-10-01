package com.example.zwi.repository;

import com.example.zwi.domain.Transaction;

import java.util.List;

public interface TransactionRepository {
    void save(Transaction transaction);

    List<Transaction> findAll();
}
