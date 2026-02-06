package com.progresssoft.warehouse.validation.validationClass;

import com.progresssoft.warehouse.validation.annotations.IsoCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

public class IsoCodeValidator implements ConstraintValidator<IsoCode, String> {

    private static final Set<String> VALID_CODES =
            Currency.getAvailableCurrencies()
                    .stream()
                    .map(Currency::getCurrencyCode)
                    .collect(Collectors.toSet());

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {

        if (value == null || value.isBlank()) return true;

        return VALID_CODES.contains(value.toUpperCase());


    }
}
