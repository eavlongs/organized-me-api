package com.organized_me.api.repository;

import com.organized_me.api.model.TrackerData;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TrackerDataRepository extends MongoRepository<TrackerData, String> {
}
