package com.organized_me.api.repository;

import com.organized_me.api.model.File;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FileRepository extends MongoRepository<File, String> {
	@Query("{ 'userId' : ?0, 'folderId' : ?1 }")
	public List<File> getFilesByUserIdAndParentId(String userId, String folderId);
}
