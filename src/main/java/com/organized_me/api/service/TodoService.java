package com.organized_me.api.service;

import com.organized_me.api.model.Todo;
import com.organized_me.api.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TodoService {
    @Autowired
    private TodoRepository todoRepository;

    public Todo addTodo(Todo todo) {
        Date now = new Date();
        todo.setCreatedAt(now);
        todo.setUpdatedAt(now);
        return todoRepository.save(todo);
    }

    public Todo updateTodo(Todo todo) {
        todo.setUpdatedAt(new Date());
        return todoRepository.save(todo);
    }

    public Page<Todo> getActiveTodos(String userId, Pageable pageable) {
        return todoRepository.findActiveTodosByUserId(userId, pageable);
    }

    public Page<Todo> getFinishedTodos(String userId, Pageable pageable) {
        return todoRepository.findFinishedTodosByUserId(userId, pageable);
    }

    public Todo getTodoById(String id) {
        return todoRepository.findById(id).orElse(null);
    }

    public void deleteTodoById(String id) {
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
        }
    }
}
