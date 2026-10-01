package com.example.zwi.api;

import com.example.zwi.compliance.LegalPolicy;
import com.example.zwi.compliance.LegalPolicyRegistry;
import com.example.zwi.domain.Listing;
import com.example.zwi.domain.ListingPriceHistoryEntry;
import com.example.zwi.domain.PriceStats;
import com.example.zwi.domain.Property;
import com.example.zwi.domain.Transaction;
import com.example.zwi.domain.TransactionFilter;
import com.example.zwi.repository.ListingPriceHistoryRepository;
import com.example.zwi.repository.ListingRepository;
import com.example.zwi.repository.PropertyRepository;
import com.example.zwi.repository.TransactionRepository;
import com.example.zwi.service.ComparablePropertyService;
import com.example.zwi.service.MapLayerService;
import com.example.zwi.service.TransactionQueryService;
import com.example.zwi.service.TransactionStatisticsService;
import com.example.zwi.service.dto.ComparableAnalysisResult;
import com.example.zwi.service.dto.MapMarker;

import java.math.BigDecimal;
import java.util.List;

public class MvpMarketAnalysisFacade {
    private final PropertyRepository propertyRepository;
    private final ListingRepository listingRepository;
    private final ListingPriceHistoryRepository listingPriceHistoryRepository;
    private final TransactionRepository transactionRepository;
    private final LegalPolicyRegistry legalPolicyRegistry;
    private final TransactionQueryService transactionQueryService;
    private final TransactionStatisticsService transactionStatisticsService;
    private final ComparablePropertyService comparablePropertyService;
    private final MapLayerService mapLayerService;

    public MvpMarketAnalysisFacade(
            PropertyRepository propertyRepository,
            ListingRepository listingRepository,
            ListingPriceHistoryRepository listingPriceHistoryRepository,
            TransactionRepository transactionRepository,
            LegalPolicyRegistry legalPolicyRegistry
    ) {
        this.propertyRepository = propertyRepository;
        this.listingRepository = listingRepository;
        this.listingPriceHistoryRepository = listingPriceHistoryRepository;
        this.transactionRepository = transactionRepository;
        this.legalPolicyRegistry = legalPolicyRegistry;
        this.transactionQueryService = new TransactionQueryService(transactionRepository);
        this.transactionStatisticsService = new TransactionStatisticsService();
        this.comparablePropertyService = new ComparablePropertyService(transactionStatisticsService);
        this.mapLayerService = new MapLayerService(propertyRepository);
    }

    public void saveProperty(Property property) {
        propertyRepository.save(property);
    }

    public void saveListing(Listing listing) {
        listingRepository.save(listing);
    }

    public void saveListingPriceHistory(ListingPriceHistoryEntry historyEntry) {
        listingPriceHistoryRepository.save(historyEntry);
    }

    public void saveTransaction(Transaction transaction) {
        transactionRepository.save(transaction);
    }

    public List<Transaction> findTransactions(TransactionFilter filter) {
        return transactionQueryService.findByFilter(filter);
    }

    public PriceStats statsForTransactions(List<Transaction> transactions) {
        return transactionStatisticsService.calculatePricePerSqmStats(transactions);
    }

    public ComparableAnalysisResult compareOfferWithTransactions(
            double latitude,
            double longitude,
            double area,
            int rooms,
            Integer floor,
            BigDecimal listingPrice,
            int monthsWindow,
            double radiusMeters,
            int maxComparables
    ) {
        return comparablePropertyService.compareListingToTransactions(
                latitude,
                longitude,
                area,
                rooms,
                floor,
                listingPrice,
                monthsWindow,
                radiusMeters,
                maxComparables,
                transactionRepository.findAll()
        );
    }

    public List<MapMarker> transactionMarkers(TransactionFilter filter) {
        return mapLayerService.buildTransactionMarkers(findTransactions(filter));
    }

    public List<MapMarker> listingMarkers() {
        return mapLayerService.buildListingMarkers(listingRepository.findAll());
    }

    public void registerLegalPolicy(LegalPolicy legalPolicy) {
        legalPolicyRegistry.register(legalPolicy);
    }

    public List<LegalPolicy> legalPolicies() {
        return legalPolicyRegistry.all();
    }
}
