package com.organized_me.api.controller;

import com.organized_me.api.dto.CreateAndEditNote;
import com.organized_me.api.model.Note;
import com.organized_me.api.model.Session;
import com.organized_me.api.service.NoteService;
import com.organized_me.api.service.SessionService;
import com.organized_me.api.service.UserService;
import com.organized_me.api.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
	@Autowired
	private SessionService sessionService;
	@Autowired
	private UserService userService;
	@Autowired
	private NoteService noteService;
	
	@PostMapping("")
	public ResponseEntity<Map<String, Object>> createNote(
			@CookieValue(value="auth_session", required = false) String sessionId,
			@Valid @RequestBody CreateAndEditNote body) {
		Session session = sessionService.getSession(sessionId);
		
		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		noteService.createNote(body, session.getUserId());
		
		return ResponseHelper.buildSuccessResponse();
	}
	
	@GetMapping("")
	public ResponseEntity<Map<String, Object>> getNotes(
			@CookieValue(value="auth_session", required = false) String sessionId) {
		Session session = sessionService.getSession(sessionId);
		
		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		List<Note> notes = noteService.getNotes(session.getUserId());
		
		Map<String, Object> data = new HashMap<>();
		data.put("notes", notes);
		
		return ResponseHelper.buildSuccessResponse(data);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<Map<String, Object>> updateNote(
			@CookieValue(value="auth_session", required = false) String sessionId,
			@PathVariable String id,
			@Valid @RequestBody CreateAndEditNote body) {
		Session session = sessionService.getSession(sessionId);
		
		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		Note note = noteService.getNote(id);
		
		if (note == null) {
			return ResponseHelper.buildNotFoundResponse();
		}
		
		if (!note.getUserId().equals(session.getUserId())) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		noteService.updateNote(note, body);
		
		return ResponseHelper.buildSuccessResponse();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Map<String, Object>> deleteNot(
			@CookieValue(value="auth_session", required = false) String sessionId,
			@PathVariable String id) {
		Session session = sessionService.getSession(sessionId);
		
		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		Note note = noteService.getNote(id);
		
		if (note == null) {
			return ResponseHelper.buildNotFoundResponse();
		}
		
		if (!note.getUserId().equals(session.getUserId())) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		noteService.deleteNote(note.getId());
		
		return ResponseHelper.buildSuccessResponse();
	}
	
}
