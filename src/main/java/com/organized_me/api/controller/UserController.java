package com.organized_me.api.controller;

import com.organized_me.api.dto.UserLoginWithProviderRequest;
import com.organized_me.api.model.Session;
import com.organized_me.api.model.User;
import com.organized_me.api.service.SessionService;
import com.organized_me.api.service.UserService;
import com.organized_me.api.util.ResponseHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
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

    @PostMapping("/login/provider")
    public ResponseEntity<Map<String, Object>> loginProvider(@RequestBody UserLoginWithProviderRequest body) {
        if (body.getProviderId() == null || body.getProviderType() == null) {
            System.out.println(body);
            return ResponseHelper.buildBadRequestResponse();
        }

        User user = userService.getUserByProviderIdAndType(body.getProviderId(), body.getProviderType());

        if (user == null) {
            User newUser = new User();
            newUser.setProviderId(body.getProviderId());
            newUser.setProviderType(body.getProviderType());
            newUser.setEmail(body.getEmail());
            newUser.setFirstName(body.getFirstName());
            newUser.setLastName(body.getLastName());
            newUser.setAvatarUrl(body.getAvatarUrl());

            user = userService.createUser(newUser);
        } else {
            user.setEmail(body.getEmail());
            user.setFirstName(body.getFirstName());
            user.setLastName(body.getLastName());
            user.setAvatarUrl(body.getAvatarUrl());
        }

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("user", user);
        return ResponseHelper.buildSuccessResponse(responseBody);
    }
    
    @PatchMapping("/feature-visibility")
    public ResponseEntity<Map<String, Object>> updateFeatureVisibility(
            @CookieValue(value="auth_session", required=false) String sessionId,
            @RequestBody List<Integer> body) {
        Session session = sessionService.getSession(sessionId);
        
        if (session == null) {
            return ResponseHelper.buildUnauthorizedResponse();
        }
        
        User user = userService.getUser(session.getUserId());
        
        if (user == null) {
            return ResponseHelper.buildUnauthorizedResponse();
        }
        
        if (body == null) {
            return ResponseHelper.buildBadRequestResponse();
        }
        
        userService.setFeatureVisibility(user, body);
        
        return ResponseHelper.buildSuccessResponse();
    }
    
    @GetMapping("/feature-visibility")
    public ResponseEntity<Map<String, Object>> getUserFeatureVisibility(
            @CookieValue(value="auth_session", required=false) String sessionId) {
        Session session = sessionService.getSession(sessionId);
        
        if (session == null) {
            return ResponseHelper.buildUnauthorizedResponse();
        }
        
        User user = userService.getUser(session.getUserId());
        
        if (user == null) {
            return ResponseHelper.buildUnauthorizedResponse();
        }
        
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("featureVisibility", user.getFeatureVisibility());
        
        return ResponseHelper.buildSuccessResponse(responseBody);
    }
    
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getUserProfile(
            @CookieValue(value="auth_session", required=false) String sessionId) {
        Session session = sessionService.getSession(sessionId);
        
        if (session == null) {
            return ResponseHelper.buildUnauthorizedResponse();
        }
        
        User user = userService.getUser(session.getUserId());
        
        if (user == null) {
            return ResponseHelper.buildUnauthorizedResponse();
        }
        
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("user", user);
        
        return ResponseHelper.buildSuccessResponse(responseBody);
    }
}
