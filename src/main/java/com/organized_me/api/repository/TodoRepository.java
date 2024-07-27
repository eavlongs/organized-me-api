package com.organized_me.api.repository;

import com.organized_me.api.model.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface TodoRepository extends MongoRepository<Todo, String> {
    @Query("{'userId': '?0', '$and': [{'finishedAt': null}, {'finishedAt': {'$exists': false}}]}")
    Page<Todo> findActiveTodosByUserId(String userId, Pageable pageable);

    @Query("{'userId': ?0, 'finishedAt': {$ne: null}}")
    Page<Todo> findFinishedTodosByUserId(String userId, Pageable pageable);
}
