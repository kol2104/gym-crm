package com.epam.gymcrm.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordUtilTest {

    @Test
    void testGetRandomPassword() {
        // Given
        int length = 10;

        // When
        String password = PasswordUtil.getRandomPassword(length);

        // Then
        assertNotNull(password);
        assertEquals(length, password.length());
        assertTrue(password.matches("[a-zA-Z0-9_]+")); // Check if password contains only characters from the alphabet
    }
}
