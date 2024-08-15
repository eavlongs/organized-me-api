package com.organized_me.api.service;

import com.organized_me.api.dto.CreateAndEditTrackerRequest;
import com.organized_me.api.model.Tracker;
import com.organized_me.api.model.TrackerData;
import com.organized_me.api.repository.TrackerDataRepository;
import com.organized_me.api.repository.TrackerRepository;
import com.organized_me.api.util.FileHandlingHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TrackerService {
	@Autowired
	private TrackerRepository trackerRepository;
	@Autowired
	private TrackerDataRepository trackerDataRepository;
	@Autowired
	private MongoTemplate mongoTemplate;
	
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
		tracker.setLargerBetter(body.isLargerBetter());
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
				FileHandlingHelper.deleteFile(tracker.getImgUrl());
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		
		if (body.getStartRange() != tracker.getDefiniteRange()[0] || body.getEndRange() != tracker.getDefiniteRange()[1]) {
			List<TrackerData> outOfBoundData = trackerDataRepository.getTrackerDataThatAreOutOfBound(tracker.getId(), body.getStartRange(), body.getEndRange());
			if (!outOfBoundData.isEmpty()) {
				throw new RuntimeException("Some existing data in the tracker are out of range");
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
		tracker.setLargerBetter(body.isLargerBetter());
		tracker.setUpdatedAt(new Date());
		
		return trackerRepository.save(tracker);
	}
	
	public void deleteTracker(Tracker tracker) throws RuntimeException {
		FileHandlingHelper.deleteFile(tracker.getImgUrl());
		trackerRepository.deleteById(tracker.getId());
	}
	
	public TrackerData getTrackerData(String trackerDataId) {
		return trackerDataRepository.findById(trackerDataId).orElse(null);
	}
	
	public void createNewDataEntry(Tracker tracker, double value) throws RuntimeException {
		if (value < tracker.getDefiniteRange()[0] || value > tracker.getDefiniteRange()[1]) {
			throw new RuntimeException("Value is out of range");
		}
		
		TrackerData trackerData;
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		
//		TrackerData todayData = trackerDataRepository.getTodayTrackerData(tracker.getId());
		Query query = new Query(Criteria.where("trackerId").is(tracker.getId()).and("createdAt").gte(today.getTime()));
		TrackerData todayData = mongoTemplate.findOne(query, TrackerData.class);
		
		if (todayData != null) {
			System.out.println(todayData.getValue());
		} else {
			System.out.println("null");
		}
		
		if (todayData != null) {
			trackerData = todayData;

		} else {
			trackerData = new TrackerData();
		}
		
		trackerData.setTrackerId(tracker.getId());
		if (tracker.isSumValueOnSameDay()) {
			trackerData.setValue(trackerData.getValue() + value);
		} else {
			trackerData.setValue(value);
		}
		
		if (todayData == null) {
			trackerData.setCreatedAt(new Date());
		}
		
		trackerData.setUpdatedAt(new Date());
		
		trackerDataRepository.save(trackerData);
	}
	
	public List<TrackerData> getTrackerData(Tracker tracker, String range) {
		Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
		String regex = "\\d+[a-zA-Z]";
		// d for day, h for hour, m for month, y for year, case-insensitive
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(range);
		
		// defaults to 7d, if the regex doesn't match
		int timeValue = 7;
		String timeUnit = "d";
		
		if (matcher.matches()) {
			// extract value and unit from string
			// explanation: if the regex matches, there is only one character at the end of the string,
			// so the value is from 0 to length - 1
			timeUnit = range.substring(range.length() - 1).toLowerCase();
			
			if (timeUnit.equals("d") || timeUnit.equals("h") || timeUnit.equals("m") || timeUnit.equals("y")) {
				timeValue = Integer.parseInt(range.substring(0, range.length() - 1));
			} else {
				timeUnit = "d";
			}
		}
		
		Calendar startDate = Calendar.getInstance();
		
		switch (timeUnit) {
			case "d":
				startDate.add(Calendar.DATE, -timeValue);
				break;
			case "h":
				startDate.add(Calendar.HOUR, -timeValue);
				break;
			case "m":
				startDate.add(Calendar.MONTH, -timeValue);
				break;
			case "y":
				startDate.add(Calendar.YEAR, -timeValue);
				break;
		}
		
		return trackerDataRepository.getTrackerDetailByTrackerId(tracker.getId(), startDate.getTime(), sort);
	}
	
	public double calculateAverage(List<TrackerData> trackerData) {
		if (trackerData.isEmpty()) {
			return 0;
		}
		
		double sum = 0;
		for (TrackerData data : trackerData) {
			sum += data.getValue();
		}
		return sum / trackerData.size();
	}
	
	public double calculateDifference(List<TrackerData> trackerData) {
		if (trackerData.isEmpty()) {
			return 0;
		}
		
		if (trackerData.size() == 1) {
			return trackerData.getFirst().getValue();
		}
		
		return trackerData.getLast().getValue() - trackerData.getFirst().getValue();
	}
	
	public double getBestValue(List<TrackerData> trackerData, boolean largerBetter) {
		if (trackerData.isEmpty()) {
			return 0;
		}
		
		if (largerBetter) {
			return getHighestValue(trackerData);
		} else {
			return getLowestValue(trackerData);
		}
	}
	
	public double getWorstValue(List<TrackerData> trackerData, boolean largerBetter) {
		if (trackerData.isEmpty()) {
			return 0;
		}
		
		if (largerBetter) {
			return getLowestValue(trackerData);
		} else {
			return getHighestValue(trackerData);
		}
	}
	
	private double getHighestValue(List<TrackerData> trackerData) {
		if (trackerData.isEmpty()) {
			return 0;
		}
		
		double highest = trackerData.getFirst().getValue();
		for (TrackerData data : trackerData) {
			if (data.getValue() > highest) {
				highest = data.getValue();
			}
		}
		return highest;
	}
	
	private double getLowestValue(List<TrackerData> trackerData) {
		if (trackerData.isEmpty()) {
			return 0;
		}
		
		double lowest = trackerData.getFirst().getValue();
		for (TrackerData data : trackerData) {
			if (data.getValue() < lowest) {
				lowest = data.getValue();
			}
		}
		return lowest;
	}
	
	public TrackerData editDataEntry(TrackerData trackerData, double value) throws RuntimeException  {
		Tracker tracker = getTracker(trackerData.getTrackerId());
		
		if (value < tracker.getDefiniteRange()[0] || value > tracker.getDefiniteRange()[1]) {
			throw new RuntimeException("Value is out of range");
		}
		trackerData.setValue(value);
		trackerData.setUpdatedAt(new Date());
		
		return trackerDataRepository.save(trackerData);
	}
	
	public void deleteDataEntry(String id) {
		trackerDataRepository.deleteById(id);
	}
}
