package com.organized_me.api.repository;

import com.organized_me.api.model.TrackerData;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;

public interface TrackerDataRepository extends MongoRepository<TrackerData, String> {
	@Query("{ 'trackerId' : ?0, 'createdAt' : { $gte: ?1 } }")
	List<TrackerData> getTrackerDetailByTrackerId(String trackerId, Date startDate,  Sort sort);
	
	@Query("{ 'trackerId' : ?0, $or: [ { 'value' : { $lt: ?1 } }, { 'value' : { $gt: ?2 } } ] }")
	List<TrackerData> getTrackerDataThatAreOutOfBound(String trackerId, double startRange, double endRange);
}
