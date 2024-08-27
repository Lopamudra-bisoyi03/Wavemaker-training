package com.todoapp.services;

import com.todoapp.models.User;

import java.util.Optional;

public interface UserService {
    void registerUser(User user);
    User  loginUser(String email, String password);
}
