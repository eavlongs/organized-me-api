package com.organized_me.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TimeConstraintValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTime {
    String message() default "The time must be at least today's date at 00:00 and at most one year from today";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}