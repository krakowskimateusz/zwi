package com.example.zwi.repository;

import com.example.zwi.domain.Property;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository {
    void save(Property property);

    Optional<Property> findById(String id);

    List<Property> findAll();
}
