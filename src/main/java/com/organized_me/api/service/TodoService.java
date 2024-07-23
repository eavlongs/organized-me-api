package com.organized_me.api.service;

import com.organized_me.api.model.Todo;
import com.organized_me.api.repository.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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

    public List<Todo> getActiveTodos(String userId) {
        return todoRepository.findActiveTodosByUserId(userId);
    }

    public List<Todo> getFinishedTodos(String userId) {
        return todoRepository.findFinishedTodosByUserId(userId);
    }

    public Todo getTodoById(String id) {
        return todoRepository.findById(id).orElse(null);
    }
}
