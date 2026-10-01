package com.example.zwi.service.dto;

public record MapMarker(
        String id,
        String type,
        double latitude,
        double longitude,
        String title,
        String subtitle
) {
}
