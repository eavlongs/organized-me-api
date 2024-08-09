package com.organized_me.api.dto;

import com.organized_me.api.validation.ValidCreateAndEditTracker;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

@ValidCreateAndEditTracker
public class CreateAndEditTrackerRequest {
	@NotNull(message = "title is required")
	@Size(min = 1, max = 50)
	private String title;
	@NotNull(message = "description is required")
	@Size(min = 1, max = 500)
	private String description;
	@NotNull(message = "name is required")
	@Size(min = 1, max = 30)
	private String name;
	@NotNull(message = "unit is required")
	@Size(min = 1, max = 30)
	private String unit;
	
	private double startRange;
	private double endRange;
	@NotNull(message = "\"Integer Only\" is required")
	private boolean integerOnly;
	@NotNull(message = "\"Sum Value If On The Same Day?\" is required")
	private boolean sumValueOnTheSameDay;
	private MultipartFile image;
	@NotNull(message = "validateImage is required")
	private boolean validateImage;
	
	public @NotNull(message = "title is required") @Size(min = 1, max = 50) String getTitle() {
		return title;
	}
	
	public void setTitle(@NotNull(message = "title is required") @Size(min = 1, max = 50) String title) {
		this.title = title;
	}
	
	public @NotNull(message = "description is required") @Size(min = 1, max = 500) String getDescription() {
		return description;
	}
	
	public void setDescription(@NotNull(message = "description is required") @Size(min = 1, max = 500) String description) {
		this.description = description;
	}
	
	public @NotNull(message = "name is required") @Size(min = 1, max = 30) String getName() {
		return name;
	}
	
	public void setName(@NotNull(message = "name is required") @Size(min = 1, max = 30) String name) {
		this.name = name;
	}
	
	public @NotNull(message = "unit is required") @Size(min = 1, max = 30) String getUnit() {
		return unit;
	}
	
	public void setUnit(@NotNull(message = "unit is required") @Size(min = 1, max = 30) String unit) {
		this.unit = unit;
	}
	
	public double getStartRange() {
		return startRange;
	}
	
	public void setStartRange(double startRange) {
		this.startRange = startRange;
	}
	
	public double getEndRange() {
		return endRange;
	}
	
	public void setEndRange(double endRange) {
		this.endRange = endRange;
	}
	
	@NotNull(message = "\"Integer Only\" is required")
	public boolean isIntegerOnly() {
		return integerOnly;
	}
	
	public void setIntegerOnly(@NotNull(message = "\"Integer Only\" is required") boolean integerOnly) {
		this.integerOnly = integerOnly;
	}
	
	@NotNull(message = "\"Sum Value If On The Same Day?\" is required")
	public boolean isSumValueOnTheSameDay() {
		return sumValueOnTheSameDay;
	}
	
	public void setSumValueOnTheSameDay(@NotNull(message = "\"Sum Value If On The Same Day?\" is required") boolean sumValueOnTheSameDay) {
		this.sumValueOnTheSameDay = sumValueOnTheSameDay;
	}
	
	public MultipartFile getImage() {
		return image;
	}
	
	public void setImage(MultipartFile image) {
		this.image = image;
	}
	
	@NotNull(message = "validateImage is required")
	public boolean isValidateImage() {
		return validateImage;
	}
	
	public void setValidateImage(@NotNull(message = "validateImage is required") boolean validateImage) {
		this.validateImage = validateImage;
	}
}
