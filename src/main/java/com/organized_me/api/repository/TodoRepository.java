package com.organized_me.api.repository;

import com.organized_me.api.model.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TodoRepository extends MongoRepository<Todo, String> {
    @Query("{'userId': '?0', '$and': [{'finishedAt': null}, {'finishedAt': {'$exists': false}}]}")
    List<Todo> findActiveTodosByUserId(String userId);

    @Query("{'userId': ?0, 'finishedAt': {$ne: null}}")
    List<Todo> findFinishedTodosByUserId(String userId);
}
