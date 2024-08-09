package com.organized_me.api.service;

import com.organized_me.api.dto.CreateAndEditTrackerRequest;
import com.organized_me.api.model.Tracker;
import com.organized_me.api.repository.TrackerRepository;
import com.organized_me.api.util.FileHandlingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TrackerService {
	@Autowired
	private TrackerRepository trackerRepository;
	
	public Tracker createTracker(CreateAndEditTrackerRequest body, String userId) throws RuntimeException {
		String imgUrl;
		try {
			imgUrl = FileHandlingHelper.uploadFile(body.getImage());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		double[] definiteRange = new double[2];
		definiteRange[0] = body.getStartRange();
		definiteRange[1] = body.getEndRange();
		
		Tracker tracker = new Tracker();
		tracker.setUserId(userId);
		tracker.setTitle(body.getTitle());
		tracker.setDescription(body.getDescription());
		tracker.setImgUrl(imgUrl);
		tracker.setName(body.getName());
		tracker.setUnit(body.getUnit());
		tracker.setDefiniteRange(definiteRange);
		tracker.setIntegerOnly(body.isIntegerOnly());
		tracker.setSumValueOnSameDay(body.isSumValueOnTheSameDay());
		tracker.setCreatedAt(new Date());
		tracker.setUpdatedAt(new Date());
		
		return trackerRepository.save(tracker);
	}
	
	public List<Tracker> getTrackers(String userId) {
		return trackerRepository.getTrackersByUserId(userId);
	}
	
	public Tracker getTracker(String trackerId) {
		return trackerRepository.findById(trackerId).orElse(null);
	}
	
	public Tracker editTracker(CreateAndEditTrackerRequest body, Tracker tracker) throws RuntimeException {
		String imgUrl = "";
		if (body.getImage() != null) {
			try {
				imgUrl = FileHandlingHelper.uploadFile(body.getImage());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		double[] definiteRange = new double[2];
		definiteRange[0] = body.getStartRange();
		definiteRange[1] = body.getEndRange();
		
		tracker.setTitle(body.getTitle());
		tracker.setDescription(body.getDescription());
		if (!imgUrl.isEmpty()) {
			tracker.setImgUrl(imgUrl);
		}
		tracker.setName(body.getName());
		tracker.setUnit(body.getUnit());
		tracker.setDefiniteRange(definiteRange);
		tracker.setIntegerOnly(body.isIntegerOnly());
		tracker.setSumValueOnSameDay(body.isSumValueOnTheSameDay());
		tracker.setUpdatedAt(new Date());
		
		return trackerRepository.save(tracker);
	}
	
	public void deleteTracker(String trackerId) {
		trackerRepository.deleteById(trackerId);
	}
}
