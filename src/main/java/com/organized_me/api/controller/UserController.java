package com.organized_me.api.controller;

import com.organized_me.api.model.Session;
import com.organized_me.api.model.User;
import com.organized_me.api.service.SessionService;
import com.organized_me.api.service.UserService;
import com.organized_me.api.util.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private SessionService sessionService;


    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}/sessions")
    public ResponseEntity<Map<String, Object>> getUserSessions(@PathVariable String userId) {
        List<Session> sessions = sessionService.getSessionsByUserId(userId);
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("sessions", sessions);
        return ResponseHelper.buildSuccessResponse(responseBody);
    }
}
