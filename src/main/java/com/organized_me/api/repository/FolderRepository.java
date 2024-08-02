package com.organized_me.api.repository;

import com.organized_me.api.model.Folder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface FolderRepository extends MongoRepository<Folder, String> {
	@Query("{ 'userId' : ?0, 'parentId' : ?1 }")
	List<Folder> getFoldersByUserIdAndParentId(String userId, String parentId);
	
	@Query("{'userId':  ?0, 'parentId':  null}")
	List<Folder> getUserRootFolders(String userId);
	
	@Query("{ 'userId' : ?1, '_id' : { $in: ?0 } }")
	List<Folder> getFoldersByIds(String[] ids, String userId);
	
	@Query("{'userId':  ?1, 'parentId':  { $in: ?0 } }")
	List<Folder> getFolderChildrenFolders(String[] ids, String userId);
}
