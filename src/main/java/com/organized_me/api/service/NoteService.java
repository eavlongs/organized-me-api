package com.organized_me.api.service;

import com.organized_me.api.dto.CreateAndEditNote;
import com.organized_me.api.model.Note;
import com.organized_me.api.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NoteService {
	@Autowired
	private NoteRepository noteRepository;
	
	public Note createNote(CreateAndEditNote body, String userId) {
		Note note = new Note();
		note.setUserId(userId);
		note.setTitle(body.getTitle());
		note.setContent("");
		Date now = new Date();
		note.setCreatedAt(now);
		note.setUpdatedAt(now);
		
		return noteRepository.save(note);
	}
	
	public List<Note> getNotes(String userId) {
		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		return noteRepository.findAllByUserId(userId, sort);
	}
	
	public Note getNote(String id) {
		return noteRepository.findById(id).orElse(null);
	}
	
	public Note updateNote(Note note, CreateAndEditNote body) {
		note.setTitle(body.getTitle());
		note.setContent(body.getContent());
		note.setUpdatedAt(new Date());
		
		return noteRepository.save(note);
	}
	
	public void deleteNote(String id) {
		noteRepository.deleteById(id);
	}
}
