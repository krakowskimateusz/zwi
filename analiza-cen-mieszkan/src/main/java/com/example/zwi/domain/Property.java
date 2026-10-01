package com.example.zwi.domain;

public record Property(
        String id,
        String address,
        String district,
        double latitude,
        double longitude,
        double area,
        int rooms,
        Integer floor,
        Integer yearBuilt,
        MarketType marketType,
        PropertyType propertyType
) {
}
