package com.organized_me.api.service;

import com.organized_me.api.model.Session;
import com.organized_me.api.repository.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    public Session getSession(String sessionId) {
        if (sessionId == null) {
            return null;
        }

        Session session = sessionRepository.findById(sessionId).orElse(null);

        if (session == null) {
            return null;
        }

        Date now = new Date();

        if (now.after(session.getExpiresAt())) {
            sessionRepository.deleteById(sessionId);
            return null;
        }

        return session;
    }

    public Session saveSession(Session session) {
        return sessionRepository.save(session);
    }

    public void deleteSession(String sessionId) {
        sessionRepository.deleteById(sessionId);
    }

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public List<Session> getSessionsByUserId(String userId) {
        return sessionRepository.findSessionsByUserId(userId);
    }

    public void deleteSessionsByUserId(String userId) {
        sessionRepository.deleteByUserId(userId);
    }

    public void deleteExpiredSessions() {
        sessionRepository.deleteExpiredSessions();
    }
}
