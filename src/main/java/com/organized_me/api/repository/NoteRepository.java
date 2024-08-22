package com.organized_me.api.repository;

import com.organized_me.api.model.Note;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface NoteRepository extends MongoRepository<Note, String> {
	List<Note> findAllByUserId(String userId, Sort sort);
}
