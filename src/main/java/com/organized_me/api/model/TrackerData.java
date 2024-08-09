package com.organized_me.api.model;

import org.springframework.data.annotation.Id;

import java.util.Date;

public class TrackerData {
	@Id
	private String id;
	private String privateId;
	private String value;
	private Date date;
	private Date createdAt;
	private Date updatedAt;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getPrivateId() {
		return privateId;
	}
	
	public void setPrivateId(String privateId) {
		this.privateId = privateId;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
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
