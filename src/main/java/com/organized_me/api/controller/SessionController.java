package com.organized_me.api.controller;

import com.organized_me.api.model.Session;
import com.organized_me.api.model.User;
import com.organized_me.api.service.SessionService;
import com.organized_me.api.service.UserService;
import com.organized_me.api.util.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserService userService;

    @GetMapping("/{sessionId}")
    public ResponseEntity<Map<String, Object>> getSessionAndUser(@PathVariable String sessionId) {
        Session session = sessionService.getSession(sessionId);
        if (session == null) {
            return ResponseHelper.buildNotFoundResponse();
        }

        User user = userService.getUserById(session.getUserId());
        if (user == null) {
            return ResponseHelper.buildNotFoundResponse();
        }

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("user", user);
        responseBody.put("session", session);
        return ResponseHelper.buildSuccessResponse(responseBody);
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createSession(@RequestBody Session session) {
        User user = userService.getUserById(session.getUserId());

        if (user == null) {
            return ResponseHelper.buildNotFoundResponse();
        }

        sessionService.saveSession(session);

        return ResponseHelper.buildSuccessResponse();
    }

    @PatchMapping("/{sessionId}")
    public ResponseEntity<Map<String, Object>> updateSession(@PathVariable String sessionId, @RequestBody Map<String, Date> body) {
        if (body.get("expiresAt") == null) {
            return ResponseHelper.buildBadRequestResponse();
        }

        Session session = sessionService.getSession(sessionId);
        if (session == null) {
            return ResponseHelper.buildNotFoundResponse();
        }

        Date expiresAt = body.get("expiresAt");
        session.setExpiresAt(expiresAt);

        return ResponseHelper.buildSuccessResponse();
    }

    @DeleteMapping("/{sessionId}")
    public ResponseEntity<Map<String, Object>> deleteSession(@PathVariable String sessionId) {
        Session session = sessionService.getSession(sessionId);
        if (session == null) {
            return ResponseHelper.buildNotFoundResponse();
        }

        sessionService.deleteSession(sessionId);
        return ResponseHelper.buildSuccessResponse();
    }

    @DeleteMapping("/{userId}/sessions")
    public ResponseEntity<Map<String, Object>> deleteUserSessions(@PathVariable String userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseHelper.buildNotFoundResponse();
        }

        sessionService.deleteSessionsByUserId(userId);

        return ResponseHelper.buildSuccessResponse();
    }

    @DeleteMapping("/expired")
    public ResponseEntity<Map<String, Object>> deleteExpiredSessions() {
        sessionService.deleteExpiredSessions();

        return ResponseHelper.buildSuccessResponse();
    }
}
