package com.organized_me.api.controller;

import com.organized_me.api.dto.CreateAndEditTodoRequest;
import com.organized_me.api.model.Session;
import com.organized_me.api.model.Todo;
import com.organized_me.api.service.SessionService;
import com.organized_me.api.service.TodoService;
import com.organized_me.api.service.UserService;
import com.organized_me.api.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    @Autowired
    private TodoService todoService;
    @Autowired
    private UserService userService;
    @Autowired
    private SessionService sessionService;

    @GetMapping("/active")
    public ResponseEntity<Map<String, Object>> getActiveTodos(@CookieValue(name="auth_session", required = false) String sessionId) {
        Session session = sessionService.getSession(sessionId);

        if (session == null) {
            return ResponseHelper.buildUnauthorizedResponse();
        }

        List<Todo> todos = todoService.getActiveTodos(session.getUserId());

        Map<String, Object> data = new HashMap<>();
        data.put("todos", todos);
        return ResponseHelper.buildSuccessResponse(data);
    }

    @GetMapping("/finished")
    public ResponseEntity<Map<String, Object>> getFinishedTodos(@CookieValue(name="auth_session", required = false) String sessionId) {
        Session session = sessionService.getSession(sessionId);

        if (session == null) {
            return ResponseHelper.buildUnauthorizedResponse();
        }

        List<Todo> todos = todoService.getFinishedTodos(session.getUserId());

        Map<String, Object> data = new HashMap<>();
        data.put("todos", todos);
        return ResponseHelper.buildSuccessResponse(data);
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createTodo(@Valid @RequestBody CreateAndEditTodoRequest body, @CookieValue(name="auth_session", required = false) String sessionId) {
        System.out.println(sessionId);
        Session session = sessionService.getSession(sessionId);

        if (session == null) {
            return ResponseHelper.buildUnauthorizedResponse();
        }

        Todo todo = new Todo();
        todo.setTitle(body.getTitle());
        todo.setDescription(body.getDescription());
        todo.setUserId(session.getUserId());
        todo.setTime(body.getTime());

        todoService.addTodo(todo);

        return ResponseHelper.buildSuccessResponse();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Map<String, Object>> editTodo(@Valid @RequestBody CreateAndEditTodoRequest body, @CookieValue(name="auth_session", required = false) String sessionId, @PathVariable String id) {
        Session session = sessionService.getSession(sessionId);

        if (session == null) {
            return ResponseHelper.buildUnauthorizedResponse();
        }

        Todo todo = todoService.getTodoById(id);

        if (todo == null) {
            return ResponseHelper.buildNotFoundResponse();
        }

        todo.setTitle(body.getTitle());
        todo.setDescription(body.getDescription());
        todo.setTime(body.getTime());

        todoService.updateTodo(todo);

        return ResponseHelper.buildSuccessResponse();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTodo(@CookieValue(name="auth_session", required = false) String sessionId, @PathVariable String id) {
        Session session = sessionService.getSession(sessionId);

        if (session == null) {
            return ResponseHelper.buildUnauthorizedResponse();
        }

        todoService.deleteTodoById(id);

        return ResponseHelper.buildSuccessResponse();
    }

    @PatchMapping("/{id}/done")
    public ResponseEntity<Map<String, Object>> markTodoAsDone(@CookieValue(name="auth_session", required = false) String sessionId, @PathVariable("id") String id) {
        Session session = sessionService.getSession(sessionId);

        if (session == null) {
            return ResponseHelper.buildUnauthorizedResponse();
        }

        Todo todo = todoService.getTodoById(id);

        if (todo == null) {
            return ResponseHelper.buildNotFoundResponse();
        }

        todo.setFinishedAt(new Date());
        todoService.updateTodo(todo);

        return ResponseHelper.buildSuccessResponse();
    }

    @PatchMapping("/{id}/restore")
    public ResponseEntity<Map<String, Object>> restoreTodo(@CookieValue(name="auth_session", required = false) String sessionId, @PathVariable("id") String id) {
        Session session = sessionService.getSession(sessionId);

        if (session == null) {
            return ResponseHelper.buildUnauthorizedResponse();
        }

        Todo todo = todoService.getTodoById(id);

        if (todo == null) {
            return ResponseHelper.buildNotFoundResponse();
        }

        todo.setFinishedAt(null);
        todoService.updateTodo(todo);

        return ResponseHelper.buildSuccessResponse();
    }
}
