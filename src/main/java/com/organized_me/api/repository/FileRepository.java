package com.organized_me.api.repository;

import com.organized_me.api.model.File;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FileRepository extends MongoRepository<File, String> {
	@Query("{ 'userId' : ?0, 'folderId' : ?1 }")
	List<File> getFilesByUserIdAndParentId(String userId, String folderId, Sort sort);
	
	@Query("{ 'userId' : ?1, 'folderId' : { $in : ?0 } }")
	List<File> getFilesByFolderIds(String[] ids, String userId);
}
