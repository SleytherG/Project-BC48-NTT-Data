package com.nttdata.msvc.client.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClientTypeTest {

    @Test
    @DisplayName("Test enum values")
    void testEnumValues() {
        assertEquals("PERSONAL", ClientType.PERSONAL.name());
        assertEquals("ENTERPRISE", ClientType.ENTERPRISE.name());
    }

    @Test
    @DisplayName("Test enum values with toString method")
    void testEnumValuesWithToString() {
        assertEquals("PERSONAL", ClientType.PERSONAL.toString());
        assertEquals("ENTERPRISE", ClientType.ENTERPRISE.toString());
    }

    @Test
    @DisplayName("Test enum ordinals")
    void testEnumOrdinals() {
        assertEquals(0, ClientType.PERSONAL.ordinal());
        assertEquals(1, ClientType.ENTERPRISE.ordinal());
    }

    @Test
    @DisplayName("Test enum values quality")
    void testEnumValuesEquality() {
        assertEquals(ClientType.PERSONAL, ClientType.PERSONAL);
        assertEquals(ClientType.ENTERPRISE, ClientType.ENTERPRISE);
    }
}
