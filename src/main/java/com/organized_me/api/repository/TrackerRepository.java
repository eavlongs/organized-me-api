package com.organized_me.api.repository;

import com.organized_me.api.model.Tracker;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface TrackerRepository extends MongoRepository<Tracker, String> {
	@Query("{ 'userId' : ?0 }")
	List<Tracker> getTrackersByUserId(String userId);
}
