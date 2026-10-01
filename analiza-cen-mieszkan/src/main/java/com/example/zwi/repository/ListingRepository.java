package com.example.zwi.repository;

import com.example.zwi.domain.Listing;

import java.util.List;

public interface ListingRepository {
    void save(Listing listing);

    List<Listing> findAll();

    List<Listing> findByPropertyId(String propertyId);
}
