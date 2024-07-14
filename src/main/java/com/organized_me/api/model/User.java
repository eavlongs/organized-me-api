package com.organized_me.api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String firstName;
    private String lastName;

    private String email;
    private String providerType;
    private String providerID;
    private Date createdAt;
    private Date updatedAt;
}
