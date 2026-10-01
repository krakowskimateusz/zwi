package com.example.zwi.compliance;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class LegalPolicyRegistry {
    private final Map<String, LegalPolicy> policies = new LinkedHashMap<>();

    public void register(LegalPolicy policy) {
        policies.put(policy.source().toLowerCase(Locale.ROOT), policy);
    }

    public Optional<LegalPolicy> find(String source) {
        return Optional.ofNullable(policies.get(source.toLowerCase(Locale.ROOT)));
    }

    public List<LegalPolicy> all() {
        return List.copyOf(policies.values());
    }
}
