package com.example.zwi.repository.inmemory;

import com.example.zwi.domain.Property;
import com.example.zwi.repository.PropertyRepository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InMemoryPropertyRepository implements PropertyRepository {
    private final Map<String, Property> properties = new LinkedHashMap<>();

    @Override
    public void save(Property property) {
        properties.put(property.id(), property);
    }

    @Override
    public Optional<Property> findById(String id) {
        return Optional.ofNullable(properties.get(id));
    }

    @Override
    public List<Property> findAll() {
        return new ArrayList<>(properties.values());
    }
}
