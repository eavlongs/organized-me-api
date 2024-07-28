package com.organized_me.api.controller;

import com.organized_me.api.model.File;
import com.organized_me.api.model.Folder;
import com.organized_me.api.model.FolderParent;
import com.organized_me.api.model.Session;
import com.organized_me.api.service.SessionService;
import com.organized_me.api.service.StorageService;
import com.organized_me.api.util.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
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
	public ResponseEntity<Map<String, Object>> getFolderDetail(@CookieValue(value = "auth_session", required = false) String sessionId, @PathVariable String id) {
		Session session = sessionService.getSession(sessionId);
		
		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		Map<String, Object> data = storageService.getFolderDetailById(session.getUserId(), id);
		
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
}
