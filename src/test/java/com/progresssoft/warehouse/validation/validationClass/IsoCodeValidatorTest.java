package com.progresssoft.warehouse.validation.validationClass;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class IsoCodeValidatorTest {

    private IsoCodeValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new IsoCodeValidator();
    }

    @Test
    void shouldAcceptValidIsoCode() {
        assertTrue(validator.isValid("USD", context));
        assertTrue(validator.isValid("EUR", context));
        assertTrue(validator.isValid("MAD", context)); 
    }

    @Test
    void shouldRejectInvalidIsoCode() {
        assertFalse(validator.isValid("ZZZ", context)); 
        assertFalse(validator.isValid("US", context));  
        assertFalse(validator.isValid("USDD", context)); 
    }

    @Test
    void shouldHandleNullOrEmpty() {
        
        
        assertTrue(validator.isValid(null, context));
        assertTrue(validator.isValid("", context));
    }
}