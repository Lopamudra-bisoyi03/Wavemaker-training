package com.wavemaker.leavemanagement.repositories;

import com.wavemaker.leavemanagement.model.LoginCredentials;

public interface LoginCredentialsRepository {
    void addLoginCredentials(LoginCredentials loginCredentials);
    void updateLoginCredentials(LoginCredentials loginCredentials);
    LoginCredentials getLoginCredentialsByEmail(String email);
}
