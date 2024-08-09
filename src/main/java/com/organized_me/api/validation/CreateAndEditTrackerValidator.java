package com.organized_me.api.validation;

import com.organized_me.api.dto.CreateAndEditTrackerRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CreateAndEditTrackerValidator implements ConstraintValidator<ValidCreateAndEditTracker, CreateAndEditTrackerRequest> {
	@Override
	public boolean isValid(CreateAndEditTrackerRequest body, ConstraintValidatorContext context) {
		boolean isRangeValid = body.getStartRange() < body.getEndRange();
		ImageValidator imageValidator = new ImageValidator();
		boolean validateImage = body.isValidateImage();
		boolean isImageValid = imageValidator.isValid(body.getImage(), context);
		
		if (isRangeValid) {
			if (validateImage) {
				return isImageValid;
			} else {
				return true;
			}
		}
		
		return false;
	}
}
