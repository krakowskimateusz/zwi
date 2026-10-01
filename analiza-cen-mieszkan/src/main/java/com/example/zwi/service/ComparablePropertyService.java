package com.example.zwi.service;

import com.example.zwi.domain.PriceStats;
import com.example.zwi.domain.Transaction;
import com.example.zwi.service.dto.ComparableAnalysisResult;
import com.example.zwi.service.dto.ComparableTransaction;
import com.example.zwi.util.GeoUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

public class ComparablePropertyService {
    private final TransactionStatisticsService statsService;

    public ComparablePropertyService(TransactionStatisticsService statsService) {
        this.statsService = statsService;
    }

    public ComparableAnalysisResult compareListingToTransactions(
            double listingLatitude,
            double listingLongitude,
            double listingArea,
            int listingRooms,
            Integer listingFloor,
            BigDecimal listingPrice,
            int monthsWindow,
            double radiusMeters,
            int maxComparables,
            List<Transaction> transactions
    ) {
        LocalDate minDate = LocalDate.now().minusMonths(monthsWindow);

        List<ComparableTransaction> comparables = transactions.stream()
                .filter(t -> !t.transactionDate().isBefore(minDate))
                .map(t -> toComparable(t, listingLatitude, listingLongitude, listingArea, listingRooms, listingFloor))
                .filter(c -> c.distanceMeters() <= radiusMeters)
                .sorted(Comparator.comparingDouble(ComparableTransaction::similarityScore).reversed())
                .limit(maxComparables)
                .toList();

        PriceStats stats = statsService.calculatePriceStats(
                comparables.stream().map(c -> c.transaction().pricePerSqm()).toList()
        );

        BigDecimal offerPricePerSqm = listingArea <= 0
                ? BigDecimal.ZERO
                : listingPrice.divide(BigDecimal.valueOf(listingArea), 2, RoundingMode.HALF_UP);

        BigDecimal deltaToMedianPct = BigDecimal.ZERO;
        if (stats.count() > 0 && stats.median().compareTo(BigDecimal.ZERO) > 0) {
            deltaToMedianPct = offerPricePerSqm
                    .subtract(stats.median())
                    .divide(stats.median(), 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
        }

        return new ComparableAnalysisResult(offerPricePerSqm, stats, deltaToMedianPct, comparables);
    }

    private ComparableTransaction toComparable(
            Transaction transaction,
            double listingLatitude,
            double listingLongitude,
            double listingArea,
            int listingRooms,
            Integer listingFloor
    ) {
        double distanceMeters = GeoUtils.distanceMeters(
                listingLatitude,
                listingLongitude,
                transaction.latitude(),
                transaction.longitude()
        );

        double locationScore = 1.0 - Math.min(distanceMeters / 3000.0, 1.0);
        double areaDiff = Math.abs(transaction.area() - listingArea);
        double areaScore = 1.0 - Math.min(areaDiff / Math.max(listingArea, 1.0), 1.0);
        double roomsScore = transaction.rooms() == listingRooms ? 1.0 : 0.5;
        double floorScore;
        if (listingFloor == null || transaction.floor() == null) {
            floorScore = 0.7;
        } else {
            floorScore = 1.0 - Math.min(Math.abs(transaction.floor() - listingFloor) / 10.0, 1.0);
        }

        double marketScore = transaction.marketType().name().isEmpty() ? 0.8 : 1.0;

        double similarityScore = (locationScore * 0.4)
                + (areaScore * 0.25)
                + (roomsScore * 0.2)
                + (floorScore * 0.1)
                + (marketScore * 0.05);

        return new ComparableTransaction(transaction, distanceMeters, similarityScore);
    }
}
