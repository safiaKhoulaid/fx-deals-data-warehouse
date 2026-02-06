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
        assertTrue(validator.isValid("MAD", context)); // الدرهم المغربي 🇲🇦
    }

    @Test
    void shouldRejectInvalidIsoCode() {
        assertFalse(validator.isValid("ZZZ", context)); // كود مخترع
        assertFalse(validator.isValid("US", context));  // قصير بزاف
        assertFalse(validator.isValid("USDD", context)); // طويل بزاف
    }

    @Test
    void shouldHandleNullOrEmpty() {
        // غالباً كنخليو @NotNull هي اللي تكلف بـ null، والـ Validator كيدوزهم باش ما يديرش Double check
        // حسب اللوجيك اللي درتي فـ الكلاس ديالك (إلا كان if (val == null) return true)
        assertTrue(validator.isValid(null, context));
        assertTrue(validator.isValid("", context));
    }
}