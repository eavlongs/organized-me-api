package com.organized_me.api.controller;

import com.organized_me.api.dto.CreateAndEditTrackerRequest;
import com.organized_me.api.model.Session;
import com.organized_me.api.model.Tracker;
import com.organized_me.api.model.TrackerData;
import com.organized_me.api.service.SessionService;
import com.organized_me.api.service.TrackerService;
import com.organized_me.api.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trackers")
public class TrackerController {
	@Autowired
	private SessionService sessionService;
	@Autowired
	private TrackerService trackerService;
	
	@GetMapping()
	public ResponseEntity<Map<String, Object>> getTrackers(@CookieValue(value = "auth_session", required = false) String sessionId) {
		Session session = sessionService.getSession(sessionId);

		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}

		Map<String, Object> response = new HashMap<>();
		response.put("trackers", trackerService.getTrackers(session.getUserId()));
		return ResponseHelper.buildSuccessResponse(response);
	}
	
	@PostMapping()
	public ResponseEntity<Map<String, Object>> createTracker(
			@CookieValue(value = "auth_session", required = false) String sessionId,
			@Valid @ModelAttribute CreateAndEditTrackerRequest body) {
		Session session = sessionService.getSession(sessionId);

		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}

		try {
			trackerService.createTracker(body, session.getUserId());
		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("message", e.getMessage());
			return ResponseHelper.buildInternalServerErrorResponse(error);
		}

		return ResponseHelper.buildSuccessResponse();
	}
	
	@PatchMapping("/{trackerId}")
	public ResponseEntity<Map<String, Object>> editTracker(
			@CookieValue(value = "auth_session", required = false) String sessionId,
			@PathVariable String trackerId,
			@Valid @ModelAttribute CreateAndEditTrackerRequest body) {
		Session session = sessionService.getSession(sessionId);
		
		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		Tracker tracker = trackerService.getTracker(trackerId);
		
		if (tracker == null) {
			return ResponseHelper.buildNotFoundResponse();
		}
		
		if (!tracker.getUserId().equals(session.getUserId())) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		try {
			trackerService.editTracker(body, tracker);
		} catch (Exception e) {
			Map<String, Object> error = new HashMap<>();
			error.put("message", e.getMessage());
			return ResponseHelper.buildInternalServerErrorResponse(error);
		}
		
		return ResponseHelper.buildSuccessResponse();
	}
	
	@DeleteMapping("/{trackerId}")
	public ResponseEntity<Map<String, Object>> deleteTracker(
			@CookieValue(value = "auth_session", required = false) String sessionId,
			@PathVariable String trackerId) {
		Session session = sessionService.getSession(sessionId);
		
		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		Tracker tracker = trackerService.getTracker(trackerId);
		
		if (tracker == null) {
			return ResponseHelper.buildNotFoundResponse();
		}
		
		if (!tracker.getUserId().equals(session.getUserId())) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		try {
			trackerService.deleteTracker(tracker);
		} catch(RuntimeException e) {
			return ResponseHelper.buildInternalServerErrorResponse();
		}
		
		return ResponseHelper.buildSuccessResponse();
	}
	
	@PostMapping("/entry/{trackerId}")
	public ResponseEntity<Map<String, Object>> createDataEntry(
			@CookieValue(value = "auth_session", required = false) String sessionId,
			@PathVariable String trackerId,
			@RequestBody Map<String, Object> body) {
		Session session = sessionService.getSession(sessionId);
		
		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		Tracker tracker = trackerService.getTracker(trackerId);
		
		if (tracker == null) {
			return ResponseHelper.buildNotFoundResponse();
		}
		
		if (!tracker.getUserId().equals(session.getUserId())) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		if (body.get("value") == null) {
			return ResponseHelper.buildBadRequestResponse();
		}
		
		double value;
		
		if (body.get("value") instanceof Integer) {
			value = (Integer) body.get("value");
		} else if (body.get("value") instanceof Double) {
			value = (Double) body.get("value");
		} else {
			return ResponseHelper.buildBadRequestResponse();
		}
		 
		try {
			trackerService.createNewDataEntry(tracker, value);
		} catch (RuntimeException e) {
			Map<String, Object> error = new HashMap<>();
			error.put("message", e.getMessage());
			return ResponseHelper.buildBadRequestResponse(error);
		}
		
		return ResponseHelper.buildSuccessResponse();
	}
	
	@GetMapping("/{trackerId}")
	public ResponseEntity<Map<String, Object>> getTrackerDetail(
			@CookieValue(value = "auth_session", required = false) String sessionId,
			@PathVariable String trackerId,
			@RequestParam(value = "range", required = false, defaultValue = "7d") String range) {
		Session session = sessionService.getSession(sessionId);
		
		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		Tracker tracker = trackerService.getTracker(trackerId);
		
		if (tracker == null) {
			return ResponseHelper.buildNotFoundResponse();
		}
		
		if (!tracker.getUserId().equals(session.getUserId())) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		List<TrackerData> trackerData = trackerService.getTrackerData(tracker, range);
		
		double average = trackerService.calculateAverage(trackerData);
		double difference = trackerService.calculateDifference(trackerData);
		double bestValue = trackerService.getBestValue(trackerData, tracker.isLargerBetter());
		double worstValue = trackerService.getWorstValue(trackerData, tracker.isLargerBetter());
		double valueRange = Math.abs(bestValue - worstValue);
		
		Map<String, Object> data = new HashMap<>();
		
		data.put("tracker", tracker);
		data.put("trackerData", trackerData);
		data.put("average", average);
		data.put("best", bestValue);
		data.put("worst", worstValue);
		data.put("range", valueRange);
		
		return ResponseHelper.buildSuccessResponse(data);
	}
	
	@PatchMapping("/entry/{trackerDataId}")
	public ResponseEntity<Map<String, Object>> editDataEntry(
			@CookieValue(value = "auth_session", required = false) String sessionId,
			@PathVariable String trackerDataId,
			@RequestBody Map<String, Object> body) {
		Session session = sessionService.getSession(sessionId);
		
		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		TrackerData trackerData = trackerService.getTrackerData(trackerDataId);
		
		if (trackerData == null) {
			return ResponseHelper.buildNotFoundResponse();
		}
		
		Tracker tracker = trackerService.getTracker(trackerData.getTrackerId());
		
		if (tracker == null) {
			return ResponseHelper.buildNotFoundResponse();
		}
		
		if (!tracker.getUserId().equals(session.getUserId())) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		if (body.get("value") == null) {
			return ResponseHelper.buildBadRequestResponse();
		}
		
		double value;
		
		if (body.get("value") instanceof Integer) {
			value = (Integer) body.get("value");
		} else if (body.get("value") instanceof Double) {
			value = (Double) body.get("value");
		} else {
			return ResponseHelper.buildBadRequestResponse();
		}
		
		try {
			trackerService.editDataEntry(trackerData, value);
		} catch (RuntimeException e) {
			Map<String, Object> error = new HashMap<>();
			error.put("message", e.getMessage());
			return ResponseHelper.buildBadRequestResponse(error);
		}
		
		return ResponseHelper.buildSuccessResponse();
	}
	
	@DeleteMapping("/entry/{trackerDataId}")
	public ResponseEntity<Map<String, Object>> deleteTrackerDataEntry(
			@CookieValue(value = "auth_session", required = false) String sessionId,
			@PathVariable String trackerDataId) {
		Session session = sessionService.getSession(sessionId);
		
		if (session == null) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		TrackerData trackerData = trackerService.getTrackerData(trackerDataId);
		
		if (trackerData == null) {
			return ResponseHelper.buildNotFoundResponse();
		}
		
		Tracker tracker = trackerService.getTracker(trackerData.getTrackerId());
		
		if (tracker == null) {
			return ResponseHelper.buildNotFoundResponse();
		}
		
		if (!tracker.getUserId().equals(session.getUserId())) {
			return ResponseHelper.buildUnauthorizedResponse();
		}
		
		trackerService.deleteDataEntry(trackerData.getId());
		
		return ResponseHelper.buildSuccessResponse();
	}
}
