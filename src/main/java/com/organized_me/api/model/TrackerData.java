package com.organized_me.api.model;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class TrackerData {
	@Id
	private String id;
	private String trackerId;
	private double value;
	private Date createdAt;
	private Date updatedAt;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getTrackerId() {
		return trackerId;
	}
	
	public void setTrackerId(String trackerId) {
		this.trackerId = trackerId;
	}
	
	public double getValue() {
		return value;
	}
	
	public void setValue(double value) {
		this.value = value;
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
