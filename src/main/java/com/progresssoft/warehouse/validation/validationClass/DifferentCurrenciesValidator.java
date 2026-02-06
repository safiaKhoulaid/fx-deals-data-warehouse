package com.progresssoft.warehouse.validation.validationClass;

import com.progresssoft.warehouse.dto.DealRequestDTO;
import com.progresssoft.warehouse.validation.annotations.DifferentCurrencies;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DifferentCurrenciesValidator implements ConstraintValidator<DifferentCurrencies, DealRequestDTO> {
    @Override
    public boolean isValid(DealRequestDTO dto, ConstraintValidatorContext context) {
        return !dto.fromCurrencyIsoCode().equalsIgnoreCase(dto.toCurrencyIsoCode());
    }
}