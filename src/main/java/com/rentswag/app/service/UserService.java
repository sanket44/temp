package com.rentswag.app.service;

import java.util.List;

import com.rentswag.app.model.User;
import com.rentswag.app.model.UserDto;

public interface UserService {
	User save(UserDto user);
    User save(User user);//User save(UserDto user);
    List<User> findAll();
    User findOne(String username);
    public boolean isEnabled();
    
}
