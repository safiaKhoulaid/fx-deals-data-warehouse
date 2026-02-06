package com.progresssoft.warehouse.validation.annotations;

import com.progresssoft.warehouse.validation.validationClass.IsoCodeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsoCodeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsoCode {

    String message() default "Invalid ISO Currency Code ";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
