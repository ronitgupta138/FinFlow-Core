package com.financeflow.validation;

import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

public class CurrencyValidator {
    private static final Set<String> AVAILABLE_CURRENCIES = Currency.getAvailableCurrencies()
            .stream()
            .map(Currency::getCurrencyCode)
            .collect(Collectors.toSet());

    public boolean isValid(String value) {
        if (value == null || value.trim().isEmpty()) {
            return false;
        }
        return AVAILABLE_CURRENCIES.contains(value.trim().toUpperCase());
    }
}
