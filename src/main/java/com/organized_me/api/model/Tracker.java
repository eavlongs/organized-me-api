package com.organized_me.api.model;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class Tracker {
	@Id
	private String id;
	private String userId;
	private String title;
	private String description;
	private String imgUrl;
	private String name;
	private String unit;
	private double[] definiteRange;
	private boolean integerOnly;
	private boolean sumValueOnSameDay;
	private Date createdAt;
	private Date updatedAt;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public boolean isIntegerOnly() {
		return integerOnly;
	}
	
	public void setIntegerOnly(boolean integerOnly) {
		this.integerOnly = integerOnly;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getImgUrl() {
		return imgUrl;
	}
	
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUnit() {
		return unit;
	}
	
	public void setUnit(String unit) {
		this.unit = unit;
	}
	
	public double[] getDefiniteRange() {
		return definiteRange;
	}
	
	public void setDefiniteRange(double[] definiteRange) {
		this.definiteRange = definiteRange;
	}
	
	public boolean getintegerOnly() {
		return integerOnly;
	}
	
	public void setintegerOnly(boolean integerOnly) throws IllegalArgumentException {
		this.integerOnly = integerOnly;
	}
	
	public boolean isSumValueOnSameDay() {
		return sumValueOnSameDay;
	}
	
	public void setSumValueOnSameDay(boolean sumValueOnSameDay) {
		this.sumValueOnSameDay = sumValueOnSameDay;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}
	
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	public Date getUpdatedAt() {
		return updatedAt;
	}
	
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
}
