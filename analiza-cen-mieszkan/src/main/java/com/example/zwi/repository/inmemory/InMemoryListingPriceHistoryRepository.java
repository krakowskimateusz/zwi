package com.example.zwi.repository.inmemory;

import com.example.zwi.domain.ListingPriceHistoryEntry;
import com.example.zwi.repository.ListingPriceHistoryRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryListingPriceHistoryRepository implements ListingPriceHistoryRepository {
    private final List<ListingPriceHistoryEntry> entries = new ArrayList<>();

    @Override
    public void save(ListingPriceHistoryEntry entry) {
        entries.add(entry);
    }

    @Override
    public List<ListingPriceHistoryEntry> findByListingId(String listingId) {
        return entries.stream()
                .filter(e -> e.listingId().equals(listingId))
                .sorted(Comparator.comparing(ListingPriceHistoryEntry::recordedAt))
                .collect(Collectors.toList());
    }
}
