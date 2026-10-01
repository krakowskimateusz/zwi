package com.example.zwi.repository.inmemory;

import com.example.zwi.domain.Listing;
import com.example.zwi.repository.ListingRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryListingRepository implements ListingRepository {
    private final List<Listing> listings = new ArrayList<>();

    @Override
    public void save(Listing listing) {
        listings.removeIf(existing -> existing.id().equals(listing.id()));
        listings.add(listing);
    }

    @Override
    public List<Listing> findAll() {
        return new ArrayList<>(listings);
    }

    @Override
    public List<Listing> findByPropertyId(String propertyId) {
        return listings.stream().filter(l -> l.propertyId().equals(propertyId)).collect(Collectors.toList());
    }
}
