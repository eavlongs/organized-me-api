package com.organized_me.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document("folders")
public class Folder {
	@Id
	private String id;
	private String userId;
	private String name;
	private String parentId;
	private FolderParent[] parents;
	@Transient
	private File[] files;
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
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getParentId() {
		return parentId;
	}
	
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	public File[] getFiles() {
		return files;
	}
	
	public void setFiles(File[] files) {
		this.files = files;
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
	
	public FolderParent[] getParents() {
		return parents;
	}
	
	public void setParents(FolderParent[] parents) {
		this.parents = parents;
	}
}

