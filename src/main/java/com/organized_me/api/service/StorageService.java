package com.organized_me.api.service;

import com.organized_me.api.model.File;
import com.organized_me.api.model.Folder;
import com.organized_me.api.model.FolderParent;
import com.organized_me.api.repository.FileRepository;
import com.organized_me.api.repository.FolderRepository;
import com.organized_me.api.util.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
	
	public List<File> uploadFiles(String folderId, String userId, MultipartFile[] files) {
		String dir = Objects.requireNonNull(env.getProperty("file.upload.dir"), "File Upload Directory must not be null");
		String dirAlias = Objects.requireNonNull(env.getProperty("file.upload.dir.alias"), "File Upload Directory Alias not be null");
		List<File> fileList = new ArrayList<>();
		
		for (MultipartFile file : files) {
			// save file to dir
			
			try {
				if (file.isEmpty()) {
					throw new Exception("File is empty");
				}
				
				UUID uuid = UUID.randomUUID();
				String fileToBeUploadedName = uuid.toString() + "." + Helper.getExtensionByStringHandling(file.getOriginalFilename());
				
				Path path = Path.of(dir, fileToBeUploadedName);
				
				try (InputStream inputStream = file.getInputStream()) {
					Files.copy(inputStream, path,
							StandardCopyOption.REPLACE_EXISTING);
					
					File fileModel = new File();
					fileModel.setUserId(userId);
					
					if (folderId.equals("root")) {
						fileModel.setFolderId(null);
					} else {
						fileModel.setFolderId(folderId);
					}
					
					fileModel.setName(file.getOriginalFilename());
					fileModel.setFilePath(Path.of(dirAlias, fileToBeUploadedName).toString());
					fileModel.setCreatedAt(new Date());
					fileModel.setUpdatedAt(new Date());
					
					fileList.add(fileModel);
				}
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
	
	public void deleteFile(String id) {
		File file = fileRepository.findById(id).orElse(null);
		
		if (file == null) {
			return;
		}
		
		Path path = Path.of(file.getFilePath());
		
		try {
			Files.delete(path);
			fileRepository.deleteById(id);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
