package com.organized_me.api.service;

import com.organized_me.api.model.User;
import com.organized_me.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    enum FeatureVisibility {
        TODO,
        STORAGE,
        TRACKER,
        NOTES;
        
        public static List<Integer> getValues() {
            List<Integer> values = new ArrayList<>();
            for (FeatureVisibility featureVisibility : FeatureVisibility.values()) {
                values.add(featureVisibility.ordinal());
            }
            return values;
        }
    }

    public User createUser(User user) {
        user.setFeatureVisibility(FeatureVisibility.getValues());
        user.setCreatedAt(new Date());
        user.setUpdatedAt(new Date());
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }

    public User getUserByProviderIdAndType(String providerId, String providerType) {
        return userRepository.getUserByProviderIdAndType(providerId, providerType);
    }
	
	public User getUser(String userId) {
        return userRepository.findById(userId).orElse(null);
	}
    
    public void setFeatureVisibility(User user, List<Integer> body) {
        List<Integer> validListOfFeatures = new ArrayList<>(List.of());
        
        for (Integer feature: body) {
            if (feature >= 0 && feature < FeatureVisibility.values().length) {
                validListOfFeatures.add(feature);
            }
        }
        
        user.setFeatureVisibility(validListOfFeatures);
        updateUser(user);
    }
}