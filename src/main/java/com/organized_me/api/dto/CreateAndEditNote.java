package com.organized_me.api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateAndEditNote {
	@NotNull(message = "Title cannot be empty")
	@Size(min=1, max=100)
	private String title;
	
	private String content;
	
	public @NotNull(message = "Title cannot be empty") @Size(min = 1, max = 100) String getTitle() {
		return title;
	}
	
	public void setTitle(@NotNull(message = "Title is required") @Size(min = 1, max = 100) String title) {
		this.title = title;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
}
