package com.nttdata.msvc.client.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClientTest {

    @Test
    void testClientCreator() {
        Client client = Client
                .builder()
                .id("1")
                .firstName("John")
                .lastName("Doe")
                .documentType("DNI")
                .documentNumber("15859658")
                .clientType(ClientType.PERSONAL)
                .build();

        assertEquals("1", client.getId());
        assertEquals("John", client.getFirstName());
        assertEquals("Doe", client.getLastName());
        assertEquals("DNI", client.getDocumentType());
        assertEquals("15859658", client.getDocumentNumber());
        assertEquals(ClientType.PERSONAL, client.getClientType());
    }

    @Test
    void testClientModification() {
        Client originalClient = Client.builder()
                .id("1")
                .firstName("John")
                .lastName("Doe")
                .documentType("DNI")
                .documentNumber("15859658")
                .clientType(ClientType.PERSONAL)
                .build();

        Client modifiedClient = originalClient.toBuilder()
                .firstName("UpdatedJohn")
                .build();

        assertEquals("UpdatedJohn", modifiedClient.getFirstName());
    }
}
