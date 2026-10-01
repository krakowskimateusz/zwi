package com.example.zwi.repository.inmemory;

import com.example.zwi.domain.Transaction;
import com.example.zwi.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;

public class InMemoryTransactionRepository implements TransactionRepository {
    private final List<Transaction> transactions = new ArrayList<>();

    @Override
    public void save(Transaction transaction) {
        transactions.add(transaction);
    }

    @Override
    public List<Transaction> findAll() {
        return new ArrayList<>(transactions);
    }
}
