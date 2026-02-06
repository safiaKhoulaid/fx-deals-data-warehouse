package com.progresssoft.warehouse.validation.validationClass;

import com.progresssoft.warehouse.dto.DealRequestDTO;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class DifferentCurrenciesValidatorTest {

    private DifferentCurrenciesValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new DifferentCurrenciesValidator();
    }

    @Test
    void shouldAcceptDifferentCurrencies() {
        DealRequestDTO dto = new DealRequestDTO("1", "USD", "MAD", Instant.now(), BigDecimal.TEN);
        assertTrue(validator.isValid(dto, context));
    }

    @Test
    void shouldRejectSameCurrencies() {
        DealRequestDTO dto = new DealRequestDTO("1", "USD", "USD", Instant.now(), BigDecimal.TEN);
        assertFalse(validator.isValid(dto, context));
    }

    @Test
    void shouldHandleNullValuesGracefully() {
        
        DealRequestDTO dto = new DealRequestDTO("1", "USD", null, Instant.now(), BigDecimal.TEN);
        assertTrue(validator.isValid(dto, context)); 
    }
}