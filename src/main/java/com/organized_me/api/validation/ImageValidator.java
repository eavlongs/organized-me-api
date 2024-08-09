package com.organized_me.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class ImageValidator implements ConstraintValidator<ValidImage, MultipartFile> {
	private static final String[] ACCEPTED_IMAGES_TYPES = {"image/jpeg", "image/jpg", "image/png", "image/webp"};
	@Override
	public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
		if (file == null) {
			return false;
		}
		String contentType = file.getContentType();
		for (String acceptedType : ACCEPTED_IMAGES_TYPES) {
			if (acceptedType.equals(contentType)) {
				return true;
			}
		}
		return false;
	}
}
