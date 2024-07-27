package com.organized_me.api.service;

import com.organized_me.api.model.File;
import com.organized_me.api.model.Folder;
import com.organized_me.api.model.FolderParent;
import com.organized_me.api.repository.FileRepository;
import com.organized_me.api.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StorageService {
	@Autowired
	private FolderRepository folderRepository;
	@Autowired
	private FileRepository fileRepository;
	
	public Folder createFolder(Folder folder) {
		folder.setCreatedAt(new Date());
		folder.setUpdatedAt(new Date());
		return folderRepository.save(folder);
	}
	
	public Folder getFolderById(String id) {
		return folderRepository.findById(id).orElse(null);
	}
	
	public Folder updateFolder(Folder folder) {
		folder.setUpdatedAt(new Date());
		return folderRepository.save(folder);
	}
	
	public void deleteFolder(String id) {
		folderRepository.deleteById(id);
	}
	
	public Map<String, Object> getFolderDetailById(String userId, String id) {
		Folder folder = null;
		
		FolderParent[] parents = new FolderParent[]{};
		if (!id.equals("root")) {
			folder = folderRepository.findById(id).orElse(null);
			
			if (folder == null || !folder.getUserId().equals(userId)) {
				return null;
			}
			parents = folder.getParents();
		}
		
		List<Folder> folders = new ArrayList<>();
		
		if (id.equals("root")) {
			folders = folderRepository.getFoldersByUserIdAndParentId(userId, null);
		} else {
			folders = folderRepository.getFoldersByUserIdAndParentId(userId, id);
		}
		
		List<File> files;
		
		if (id.equals("root")) {
			files = fileRepository.getFilesByUserIdAndParentId(userId, null);
		} else {
			files = fileRepository.getFilesByUserIdAndParentId(userId, id);
		}
		
		Map<String, Object> data =  new HashMap<>();
		data.put("folder", folder);
		data.put("parents", parents);
		data.put("folders", folders);
		data.put("files", files);
		
		return data;
	}
}
