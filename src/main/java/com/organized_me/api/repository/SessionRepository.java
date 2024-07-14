package com.organized_me.api.repository;

import com.organized_me.api.model.Session;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SessionRepository extends MongoRepository<Session, String> {
    @Query("{userId:'?0'}")
    List<Session> findSessionsByUserId(String userId);

    @Query("{userId:'?0'}")
    void deleteByUserId(String userId);

    @Query("{expiresAt: { $lt : ?0 }}")
    void deleteExpiredSessions();
}
