package com.example.zwi.compliance;

public record LegalPolicy(
        String source,
        boolean officialApiAvailable,
        boolean scrapingAllowed,
        boolean storageAllowed,
        boolean commercialUseAllowed,
        boolean linkToOriginalAllowed,
        String notes
) {
}
