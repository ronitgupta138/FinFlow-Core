package com.financeflow.service;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class DoubleEntryTransactionBuilderTest {
    @Test
    void testBalancedTransactionBuildsSuccessfully() {
        DoubleEntryTransactionBuilder builder = new DoubleEntryTransactionBuilder();
        var entries = builder
            .addDebit("ACC_CASH", new BigDecimal("500.00"))
            .addCredit("ACC_REVENUE", new BigDecimal("500.00"))
            .build();

        assertEquals(2, entries.size());
    }

    @Test
    void testUnbalancedTransactionThrows() {
        DoubleEntryTransactionBuilder builder = new DoubleEntryTransactionBuilder();
        builder.addDebit("ACC_CASH", new BigDecimal("500.00"))
               .addCredit("ACC_REVENUE", new BigDecimal("490.00"));

        assertThrows(IllegalStateException.class, builder::build);
    }
}
