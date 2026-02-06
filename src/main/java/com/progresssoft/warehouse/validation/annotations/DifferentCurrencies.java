package com.progresssoft.warehouse.validation.annotations;

import com.progresssoft.warehouse.validation.validationClass.DifferentCurrenciesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = DifferentCurrenciesValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DifferentCurrencies {
    String message() default "From and To currencies must be different";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}