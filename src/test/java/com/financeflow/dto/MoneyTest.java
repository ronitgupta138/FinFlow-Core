package com.financeflow.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class MoneyTest {
    @Test
    void testScaleAndRounding() {
        Money m = Money.of(100.555, "INR");
        assertEquals(new BigDecimal("100.56"), m.getAmount());
        assertEquals("INR", m.getCurrency());
    }

    @Test
    void testAdditionAndSubtraction() {
        Money a = Money.of("50.25", "USD");
        Money b = Money.of("49.75", "USD");
        Money sum = a.add(b);
        assertEquals(new BigDecimal("100.00"), sum.getAmount());

        Money diff = a.subtract(b);
        assertEquals(new BigDecimal("0.50"), diff.getAmount());
    }

    @Test
    void testCurrencyMismatchThrows() {
        Money inr = Money.of("100.00", "INR");
        Money usd = Money.of("100.00", "USD");
        assertThrows(IllegalArgumentException.class, () -> inr.add(usd));
    }

    @Test
    void testComparison() {
        Money a = Money.of("50.00", "EUR");
        Money b = Money.of("75.00", "EUR");
        assertTrue(a.compareTo(b) < 0);
        assertTrue(b.compareTo(a) > 0);
    }
}
