package com.example.zwi.service;

import com.example.zwi.domain.Listing;
import com.example.zwi.domain.Property;
import com.example.zwi.domain.Transaction;
import com.example.zwi.repository.PropertyRepository;
import com.example.zwi.service.dto.MapMarker;

import java.util.ArrayList;
import java.util.List;

public class MapLayerService {
    private final PropertyRepository propertyRepository;

    public MapLayerService(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public List<MapMarker> buildTransactionMarkers(List<Transaction> transactions) {
        List<MapMarker> markers = new ArrayList<>();
        for (Transaction t : transactions) {
            markers.add(new MapMarker(
                    t.id(),
                    "transaction",
                    t.latitude(),
                    t.longitude(),
                    t.district(),
                    t.price() + " | " + t.pricePerSqm() + " zł/m²"
            ));
        }
        return markers;
    }

    public List<MapMarker> buildListingMarkers(List<Listing> listings) {
        List<MapMarker> markers = new ArrayList<>();
        for (Listing listing : listings) {
            Property property = propertyRepository.findById(listing.propertyId()).orElse(null);
            if (property == null) {
                continue;
            }
            markers.add(new MapMarker(
                    listing.id(),
                    "listing",
                    property.latitude(),
                    property.longitude(),
                    property.district(),
                    listing.price() + " | " + listing.source()
            ));
        }
        return markers;
    }
}
