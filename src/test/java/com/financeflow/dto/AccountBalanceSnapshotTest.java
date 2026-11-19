package com.financeflow.dto;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class AccountBalanceSnapshotTest {
    @Test
    void testSnapshotCreation() {
        AccountBalanceSnapshot snap = new AccountBalanceSnapshot(
            "ACC_1001", new BigDecimal("5000.00"), new BigDecimal("4800.00"), "INR", System.currentTimeMillis()
        );
        assertEquals("ACC_1001", snap.accountId());
        assertEquals("INR", snap.currency());
        assertEquals(new BigDecimal("5000.00"), snap.ledgerBalance());
    }
}
