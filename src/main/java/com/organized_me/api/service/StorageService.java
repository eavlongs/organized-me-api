package com.organized_me.api.service;

import com.organized_me.api.model.File;
import com.organized_me.api.model.Folder;
import com.organized_me.api.model.FolderParent;
import com.organized_me.api.repository.FileRepository;
import com.organized_me.api.repository.FolderRepository;
import com.organized_me.api.util.FileHandlingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.util.*;

@Service
public class StorageService {
	@Autowired
	private FolderRepository folderRepository;
	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private Environment env;
	
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
	
	public Map<String, Object> getFolderDetailById(String userId, String id, Sort sort) {
		Folder folder = null;
		
		FolderParent[] parents = new FolderParent[]{};
		if (!id.equals("root")) {
			folder = folderRepository.findById(id).orElse(null);
			
			if (folder == null || !folder.getUserId().equals(userId)) {
				return null;
			}
			parents = folder.getParents();
		}
		
		List<Folder> folders;
		
		if (id.equals("root")) {
			folders = folderRepository.getFoldersByUserIdAndParentId(userId, null, sort);
		} else {
			folders = folderRepository.getFoldersByUserIdAndParentId(userId, id, sort);
		}
		
		List<File> files;
		
		if (id.equals("root")) {
			files = fileRepository.getFilesByUserIdAndParentId(userId, null, sort);
		} else {
			files = fileRepository.getFilesByUserIdAndParentId(userId, id, sort);
		}
		
		Map<String, Object> data =  new HashMap<>();
		data.put("folder", folder);
		data.put("parents", parents);
		data.put("folders", folders);
		data.put("files", files);
		
		return data;
	}
	
	public List<File> uploadFiles(String folderId, String userId, MultipartFile[] files) {
		List<File> fileList = new ArrayList<>();
		
		for (MultipartFile file : files) {
			try {
				String filePath = FileHandlingHelper.uploadFile(file);
				File fileModel = new File();
				fileModel.setUserId(userId);
				
				if (folderId.equals("root")) {
					fileModel.setFolderId(null);
				} else {
					fileModel.setFolderId(folderId);
				}
				
				fileModel.setName(file.getOriginalFilename());
				fileModel.setFilePath(filePath);
				fileModel.setCreatedAt(new Date());
				fileModel.setUpdatedAt(new Date());
				
				fileList.add(fileModel);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		return fileRepository.saveAll(fileList);
	}
	
	public File getFileById(String id) {
		return fileRepository.findById(id).orElse(null);
	}
	
	public File updateFile(File file) {
		file.setUpdatedAt(new Date());
		return fileRepository.save(file);
	}
	
	public void deleteFile(String id) throws Exception {
		File file = fileRepository.findById(id).orElse(null);
		
		if (file == null) {
			return;
		}
		
		try {
			FileHandlingHelper.deleteFile(file.getFilePath());
			fileRepository.deleteById(id);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
	
	public List<Folder> getFoldersByIds(String[] ids, String userId) {
		return folderRepository.getFoldersByIds(ids, userId);
	}
	
	public List<Folder> getFolderChildrenFolders(String[] ids, String userId) {
		return folderRepository.getFolderChildrenFolders(ids, userId);
	}
	
	public List<Folder> getUserRootFolders(String userId) {
		return folderRepository.getUserRootFolders(userId);
	}
	
	public List<File> getFilesByFolderIds(String[] ids, String userId) {
		return fileRepository.getFilesByFolderIds(ids, userId);
	}
	
	public Resource downloadFile(File file) throws MalformedURLException {
		return FileHandlingHelper.downloadFile(file);
	}
}
