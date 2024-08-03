package com.organized_me.api.controller;

import com.organized_me.api.model.File;
import com.organized_me.api.model.Folder;
import com.organized_me.api.model.FolderParent;
import com.organized_me.api.model.Session;
import com.organized_me.api.service.SessionService;
import com.organized_me.api.service.StorageService;
import com.organized_me.api.util.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/storage")
public class StorageController {
	@Autowired
	private StorageService storageService;
	@Autowired
	private SessionService sessionService;
	
	@GetMapping("/folder/{id}")
	public ResponseEntity<Map<String, Object>> getFolderDetail(
			@CookieValue(value = "auth_session", required = false) String sessionId,
			@PathVariable String id,
			@RequestParam(value = "sort", defaultValue = "asc", required = false) String sortDirection,
			@RequestParam(value = "sortKey", defaultValue = "name", required = false) String sortKey) {
		
		Session session = sessionService.getSession(sessionId);
		
		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		Sort sort = Sort.by(sortKey);
		
		if (sortDirection.equals("desc")) {
			sort = sort.descending();
		} else if (sortDirection.equals("asc")) {
			sort = sort.ascending();
		}
		
		Map<String, Object> data = storageService.getFolderDetailById(session.getUserId(), id, sort);
		
		if (data == null) {
			return ResponseHelper.buildNotFoundResponse();
		}
		
		return ResponseHelper.buildSuccessResponse(data);
	}
	
	@PostMapping("/folder/{id}")
	public ResponseEntity<Map<String, Object>> createFolder(
			@CookieValue(value = "auth_session", required = false) String sessionId,
			@RequestBody Map<String, Object> body,
			@PathVariable String id) {
		Session session = sessionService.getSession(sessionId);
		
		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		if (body.get("name") == null) {
			return ResponseHelper.buildBadRequestResponse();
		}
		
		Folder folder = new Folder();
		
		
		if (id.equals("root")) {
			folder.setParentId(null);
			folder.setParents(new FolderParent[]{});
		} else {
			folder.setParentId(id);
			Folder parentFolder = storageService.getFolderById(id);
			
			if (parentFolder == null) {
				return ResponseHelper.buildNotFoundResponse();
			}
			
			List<FolderParent> parents = new ArrayList<>(List.of(parentFolder.getParents()));
			FolderParent folderParent = new FolderParent();
			
			folderParent.setId(parentFolder.getId());
			folderParent.setName(parentFolder.getName());
			
			parents.add(folderParent);
			
			FolderParent[] folderParents = new FolderParent[parents.size()];
			folderParents = parents.toArray(folderParents);
			
			folder.setParents(folderParents);
		}
		
		folder.setName(body.get("name").toString());
		
		folder.setUserId(session.getUserId());
		
		storageService.createFolder(folder);
		
		return ResponseHelper.buildSuccessResponse();
	}
	
	@PatchMapping("/folder/{id}")
	public ResponseEntity<Map<String, Object>> editFolder(
			@CookieValue(value = "auth_session", required = false) String sessionId,
			@RequestBody Map<String, Object> body,
			@PathVariable String id) {
		Session session = sessionService.getSession(sessionId);
		
		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		if (body.get("name") == null) {
			return ResponseHelper.buildBadRequestResponse();
		}
		
		Folder folder = storageService.getFolderById(id);
		
		if (folder == null) {
			return ResponseHelper.buildNotFoundResponse();
		}
		
		folder.setName(body.get("name").toString());
		
		storageService.updateFolder(folder);
		
		return ResponseHelper.buildSuccessResponse();
	}
	
	@DeleteMapping("/folder/{id}")
	public ResponseEntity<Map<String, Object>> deleteFolder(
			@CookieValue(value = "auth_session", required = false) String sessionId,
			@PathVariable String id) {
		Session session = sessionService.getSession(sessionId);
		
		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		Folder folder = storageService.getFolderById(id);
		
		if (folder == null) {
			return ResponseHelper.buildNotFoundResponse();
		}
		
		storageService.deleteFolder(folder.getId());
		
		return ResponseHelper.buildSuccessResponse();
	}
	
	@PostMapping("/file/{folderId}")
	public ResponseEntity<Map<String, Object>> uploadFile(
			@CookieValue(value = "auth_session", required = false) String sessionId,
			@PathVariable String folderId,
			@RequestParam("files") MultipartFile[] body) {
		Session session = sessionService.getSession(sessionId);
		
		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		if (body.length == 0) {
			return ResponseHelper.buildBadRequestResponse();
		}
		
		Folder folder = storageService.getFolderById(folderId);
		
		if (folder == null && !folderId.equals("root")) {
			return ResponseHelper.buildNotFoundResponse();
		}
		
		if (folder != null && !folder.getUserId().equals(session.getUserId())) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		try{
			storageService.uploadFiles(folderId, session.getUserId(), body);
		} catch(Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("file_upload", e.toString());
			return ResponseHelper.buildBadRequestResponse(error);
		}
		
		return ResponseHelper.buildSuccessResponse();
	}
	
	@PatchMapping("/file/{id}")
	public ResponseEntity<Map<String, Object>> editFile(
			@CookieValue(value = "auth_session", required = false) String sessionId,
			@PathVariable String id,
			@RequestBody Map<String, Object> body) {
		Session session = sessionService.getSession(sessionId);
		
		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		if (body.get("name") == null) {
			return ResponseHelper.buildBadRequestResponse();
		}
		
		File file = storageService.getFileById(id);
		
		if (file == null || !file.getUserId().equals(session.getUserId())) {
			return ResponseHelper.buildNotFoundResponse();
		}
		
		file.setName(body.get("name").toString());
		
		storageService.updateFile(file);
		
		return ResponseHelper.buildSuccessResponse();
	}
	
	@DeleteMapping("/file/{id}")
	public ResponseEntity<Map<String, Object>> deleteFile(
			@CookieValue(value = "auth_session", required = false) String sessionId,
			@PathVariable String id) {
		Session session = sessionService.getSession(sessionId);
		
		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		File file = storageService.getFileById(id);
		
		if (file == null || !file.getUserId().equals(session.getUserId())) {
			return ResponseHelper.buildNotFoundResponse();
		}
		
		storageService.deleteFile(file.getId());
		
		return ResponseHelper.buildSuccessResponse();
	}
	
	@GetMapping("/folder/expandable")
	public ResponseEntity<Map<String, Object>> getExpandableFolderStructure(
			@CookieValue(value = "auth_session", required = false) String sessionId,
			@RequestParam(value = "ids") String[] ids) {
		Session session = sessionService.getSession(sessionId);
		
		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		List<Folder> folders = storageService.getFolderChildrenFolders(ids, session.getUserId());
		
		if (folders == null) {
			return ResponseHelper.buildNotFoundResponse();
		}
		
		List<Folder> rootFolder = storageService.getUserRootFolders(session.getUserId());
		
		List<String> IdsWithNull = new ArrayList<>(List.of(ids));
		IdsWithNull.add(null);
		
		List<File> files = storageService.getFilesByFolderIds(IdsWithNull.toArray(new String[0]), session.getUserId());
		
		List<File> rootFiles = new ArrayList<>();
		
		for (File file: files) {
			if (file.getFolderId() == null) {
				rootFiles.add(file);
				continue;
			}
			for (Folder folder: folders) {
				if (folder.getId().equals(file.getFolderId())) {
					List<File> tmpFiles = new ArrayList<>(List.of(folder.getFiles()));
					tmpFiles.add(file);
					
					folder.setFiles(tmpFiles.toArray(new File[0]));
					break;
				}
			}
		}
		
		Map<String, Object> data = new HashMap<>();
		data.put("rootFolders", rootFolder);
		data.put("rootFiles", rootFiles);
		data.put("folders", folders);
		
		return ResponseHelper.buildSuccessResponse(data);
	}
	
	@PatchMapping("/file/move")
	public ResponseEntity<Map<String, Object>> moveFile(
			@CookieValue(value = "auth_session", required = false) String sessionId,
			@RequestBody Map<String, Object> body) {
		Session session = sessionService.getSession(sessionId);
		
		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		if (body.get("id") == null || body.get("folderId") == null) {
			return ResponseHelper.buildBadRequestResponse();
		}
		
		File file = storageService.getFileById(body.get("id").toString());
		
		if (file == null || !file.getUserId().equals(session.getUserId())) {
			return ResponseHelper.buildNotFoundResponse();
		}
		
		Folder folder = storageService.getFolderById(body.get("folderId").toString());
		
		if (body.get("folderId").toString().equals("root")) {
			folder = new Folder();
			folder.setId(null);
		} else if (folder == null || !folder.getUserId().equals(session.getUserId())) {
			return ResponseHelper.buildNotFoundResponse();
		}
		
		file.setFolderId(folder.getId());
		
		storageService.updateFile(file);
		
		return ResponseHelper.buildSuccessResponse();
	}
	
	@PatchMapping("/folder/move")
	public ResponseEntity<Map<String, Object>> moveFolder(
			@CookieValue(value = "auth_session", required = false) String sessionId,
			@RequestBody Map<String, Object> body) {
		Session session = sessionService.getSession(sessionId);
		
		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		if (body.get("id") == null || body.get("folderId") == null) {
			return ResponseHelper.buildBadRequestResponse();
		}
		
		Folder folder = storageService.getFolderById(body.get("id").toString());
		
		if (folder == null || !folder.getUserId().equals(session.getUserId())) {
			return ResponseHelper.buildNotFoundResponse();
		}
		
		Folder destinationFolder = storageService.getFolderById(body.get("folderId").toString());
		
		if (body.get("folderId").toString().equals("root")) {
			destinationFolder = new Folder();
			destinationFolder.setId(null);
		} else if (destinationFolder == null || !destinationFolder.getUserId().equals(session.getUserId())) {
			return ResponseHelper.buildNotFoundResponse();
		}
		
		// make sure that the id and folderId is not the same and the destination is not a children of the folder
		
		if (folder.getId().equals(destinationFolder.getId())) {
			return ResponseHelper.buildBadRequestResponse();
		}
		
		FolderParent[] destinationFolderParents = destinationFolder.getParents();
		
		if (destinationFolderParents == null) {
			destinationFolderParents = new FolderParent[]{};
		}
		
		List<FolderParent> parents = new ArrayList<>(List.of(destinationFolderParents));
		
		for (FolderParent parent: parents) {
			if (parent.getId().equals(folder.getId())) {
				Map<String, Object> err = new HashMap<>();
				err.put("message", "Destination folder is a children of the folder");
				return ResponseHelper.buildBadRequestResponse(err);
			}
		}
		
		FolderParent folderParent = new FolderParent();
		
		folderParent.setId(destinationFolder.getId());
		folderParent.setName(destinationFolder.getName());
		
		parents.add(folderParent);
		
		FolderParent[] folderParents = new FolderParent[parents.size()];
		folderParents = parents.toArray(folderParents);
		
		folder.setParents(folderParents);
		folder.setParentId(destinationFolder.getId());
		
		storageService.updateFolder(folder);
		
		return ResponseHelper.buildSuccessResponse();
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity<?> downloadFile(
			@CookieValue(value="auth_session", required=false) String sessionId,
			@PathVariable String id) {
		Session session = sessionService.getSession(sessionId);
		
		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		File file = storageService.getFileById(id);
		
		if (file == null || !file.getUserId().equals(session.getUserId())) {
			return ResponseHelper.buildNotFoundResponse();
		}
		
		try {
			Resource resource = storageService.downloadFile(file);
			
			return ResponseEntity.ok()
					.contentLength(resource.contentLength())
					.contentType(MediaType.APPLICATION_OCTET_STREAM)
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
					.body(resource);
		} catch (Exception e) {
			return ResponseHelper.buildBadRequestResponse();
		}
	}
}
