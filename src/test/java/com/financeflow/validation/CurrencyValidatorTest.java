package com.financeflow.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CurrencyValidatorTest {
    private CurrencyValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CurrencyValidator();
    }

    @Test
    void testValidCurrencies() {
        assertTrue(validator.isValid("INR"));
        assertTrue(validator.isValid("USD"));
        assertTrue(validator.isValid("EUR"));
        assertTrue(validator.isValid("GBP"));
        assertTrue(validator.isValid("jpy")); // case-insensitive check
    }

    @Test
    void testInvalidCurrencies() {
        assertFalse(validator.isValid(null));
        assertFalse(validator.isValid(""));
        assertFalse(validator.isValid("   "));
        assertFalse(validator.isValid("INVALID"));
        assertFalse(validator.isValid("XYZ123"));
    }
}
