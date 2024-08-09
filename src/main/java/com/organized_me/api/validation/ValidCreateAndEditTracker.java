package com.organized_me.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CreateAndEditTrackerValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCreateAndEditTracker {
	String message() default "Invalid tracker data";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}