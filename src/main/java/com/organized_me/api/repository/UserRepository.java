package com.organized_me.api.repository;

import com.organized_me.api.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> {
    @Query("{providerId:?0,providerType:?1}")
    User getUserByProviderIdAndType(String providerId, String providerType);
}
