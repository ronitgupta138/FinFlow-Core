package com.financeflow.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Immutable point-in-time account balance snapshot.
 */
public record AccountBalanceSnapshot(
    String accountId,
    BigDecimal ledgerBalance,
    BigDecimal availableBalance,
    String currency,
    long timestampMs
) implements Serializable {
    public AccountBalanceSnapshot {
        Objects.requireNonNull(accountId, "accountId cannot be null");
        Objects.requireNonNull(ledgerBalance, "ledgerBalance cannot be null");
        Objects.requireNonNull(availableBalance, "availableBalance cannot be null");
        Objects.requireNonNull(currency, "currency cannot be null");
    }
}
