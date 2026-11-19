package com.financeflow.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Enforces strict double-entry zero-sum mathematical balancing invariants.
 */
public class DoubleEntryTransactionBuilder {
    public record Entry(String accountId, BigDecimal debit, BigDecimal credit) {}

    private final List<Entry> entries = new ArrayList<>();

    public DoubleEntryTransactionBuilder addDebit(String accountId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Debit amount must be positive");
        }
        entries.add(new Entry(accountId, amount, BigDecimal.ZERO));
        return this;
    }

    public DoubleEntryTransactionBuilder addCredit(String accountId, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Credit amount must be positive");
        }
        entries.add(new Entry(accountId, BigDecimal.ZERO, amount));
        return this;
    }

    public List<Entry> build() {
        if (entries.size() < 2) {
            throw new IllegalStateException("Double-entry transaction requires at least 2 entries");
        }
        BigDecimal totalDebit = BigDecimal.ZERO;
        BigDecimal totalCredit = BigDecimal.ZERO;

        for (Entry e : entries) {
            totalDebit = totalDebit.add(e.debit());
            totalCredit = totalCredit.add(e.credit());
        }

        if (totalDebit.compareTo(totalCredit) != 0) {
            throw new IllegalStateException("Unbalanced transaction: Debits (" + totalDebit + ") != Credits (" + totalCredit + ")");
        }

        return List.copyOf(entries);
    }
}
