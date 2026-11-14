package com.financeflow.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

public class CurrencyValidator implements ConstraintValidator<ValidCurrency, String> {
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

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return isValid(value);
    }
}
