package com.financeflow.audit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuditLogEntryHasherTest {
    @Test
    void testGenesisHashCreation() {
        String h1 = AuditLogEntryHasher.computeChainHash(null, "TX_1001_DEBIT_500");
        assertNotNull(h1);
        assertEquals(64, h1.length());
    }

    @Test
    void testChainedHashDeterminism() {
        String h1 = AuditLogEntryHasher.computeChainHash(null, "TX_1");
        String h2 = AuditLogEntryHasher.computeChainHash(h1, "TX_2");
        String h2Repeat = AuditLogEntryHasher.computeChainHash(h1, "TX_2");

        assertEquals(h2, h2Repeat);
        assertNotEquals(h1, h2);
    }
}
