package com.organized_me.api.util;

import com.organized_me.api.service.SessionService;
import com.organized_me.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Validation {
    @Autowired
    private SessionService sessionService;
    @Autowired
    private UserService userService;


}
