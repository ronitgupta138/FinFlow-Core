package com.financeflow.validation;

import java.math.BigDecimal;

/**
 * Validates account withdrawal limits against available balance and optional credit line.
 */
public class OverdraftProtectionValidator {
    public static boolean canWithdraw(BigDecimal currentBalance, BigDecimal withdrawalAmount, BigDecimal approvedCreditLine) {
        if (currentBalance == null || withdrawalAmount == null) {
            return false;
        }
        if (withdrawalAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        BigDecimal creditLine = (approvedCreditLine != null) ? approvedCreditLine : BigDecimal.ZERO;
        BigDecimal availableFunds = currentBalance.add(creditLine);

        return availableFunds.compareTo(withdrawalAmount) >= 0;
    }
}
