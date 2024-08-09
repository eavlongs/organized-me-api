package com.organized_me.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Constraint(validatedBy = ImageValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidImage {
	String message() default "The image must be a valid image file";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
