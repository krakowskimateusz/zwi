package com.example.zwi.compliance;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LegalPolicyRegistryTest {

    @Test
    void shouldStoreAndLoadPolicy() {
        LegalPolicyRegistry registry = new LegalPolicyRegistry();
        registry.register(new LegalPolicy("RCN", true, false, true, true, true, "official service"));

        assertEquals(1, registry.all().size());
        assertTrue(registry.find("rcn").isPresent());
        assertEquals("official service", registry.find("RCN").orElseThrow().notes());
    }
}
