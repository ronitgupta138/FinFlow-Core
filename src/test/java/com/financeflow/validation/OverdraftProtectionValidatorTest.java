package com.financeflow.validation;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

public class OverdraftProtectionValidatorTest {
    @Test
    void testWithdrawalWithinBalance() {
        assertTrue(OverdraftProtectionValidator.canWithdraw(
            new BigDecimal("1000.00"), new BigDecimal("500.00"), BigDecimal.ZERO
        ));
    }

    @Test
    void testWithdrawalWithOverdraftCreditLine() {
        assertTrue(OverdraftProtectionValidator.canWithdraw(
            new BigDecimal("100.00"), new BigDecimal("300.00"), new BigDecimal("500.00")
        ));
    }

    @Test
    void testWithdrawalExceedingLimitFails() {
        assertFalse(OverdraftProtectionValidator.canWithdraw(
            new BigDecimal("100.00"), new BigDecimal("800.00"), new BigDecimal("500.00")
        ));
    }
}
