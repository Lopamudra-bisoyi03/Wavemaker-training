package com.wavemaker.leavemanagement.service;

import com.wavemaker.leavemanagement.model.LoginCredentials;

public interface LoginCredentialsService {
    void addLoginCredentials(LoginCredentials loginCredentials);
    void updateLoginCredentials(LoginCredentials loginCredentials);
    void deleteLoginCredentials(int loginCredentialsId);
    LoginCredentials getLoginCredentialsByEmail(String email);
}
