package com.example.zwi.repository;

import com.example.zwi.domain.ListingPriceHistoryEntry;

import java.util.List;

public interface ListingPriceHistoryRepository {
    void save(ListingPriceHistoryEntry entry);

    List<ListingPriceHistoryEntry> findByListingId(String listingId);
}
