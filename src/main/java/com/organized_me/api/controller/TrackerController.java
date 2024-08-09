package com.organized_me.api.controller;

import com.organized_me.api.dto.CreateAndEditTrackerRequest;
import com.organized_me.api.model.Session;
import com.organized_me.api.model.Tracker;
import com.organized_me.api.service.SessionService;
import com.organized_me.api.service.TrackerService;
import com.organized_me.api.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
		
		trackerService.deleteTracker(tracker.getId());
		
		return ResponseHelper.buildSuccessResponse();
	}
}
