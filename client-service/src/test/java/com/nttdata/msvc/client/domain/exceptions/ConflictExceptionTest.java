package com.nttdata.msvc.client.domain.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class ConflictExceptionTest {

    @Test
    void testConstructorWithDetail() {
        // Arrange
        String detail = "This is a conflict detail";

        // Act
        ConflictException conflictException = new ConflictException(detail);

        // Assert
        assertNotNull(conflictException);
        assertEquals("Conflict Exception. This is a conflict detail", conflictException.getMessage());
    }

    @Test
    void testConstructorWithoutDetail() {
        // Act
        ConflictException conflictException = new ConflictException(null);

        // Assert
        assertNotNull(conflictException);
        assertEquals("Conflict Exception. null", conflictException.getMessage());
    }
}
