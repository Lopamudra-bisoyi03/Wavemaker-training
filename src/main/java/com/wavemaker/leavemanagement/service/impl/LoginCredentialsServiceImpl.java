package com.wavemaker.leavemanagement.service.impl;

import com.wavemaker.leavemanagement.model.LoginCredentials;
import com.wavemaker.leavemanagement.repositories.LoginCredentialsRepository;
import com.wavemaker.leavemanagement.service.LoginCredentialsService;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginCredentialsServiceImpl implements LoginCredentialsService {
    private static final Logger LOGGER = Logger.getLogger(LoginCredentialsServiceImpl.class.getName());
    private final LoginCredentialsRepository loginCredentialsRepository;

    public LoginCredentialsServiceImpl(LoginCredentialsRepository loginCredentialsRepository) {
        this.loginCredentialsRepository = loginCredentialsRepository;
    }


    @Override
    public void addLoginCredentials(LoginCredentials loginCredentials) {

    }

    @Override
    public void updateLoginCredentials(LoginCredentials loginCredentials) {

    }

    @Override
    public void deleteLoginCredentials(int loginCredentialsId) {

    }

    @Override
    public LoginCredentials getLoginCredentialsByEmail(String email) {
        try {
            return loginCredentialsRepository.getLoginCredentialsByEmail(email);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getLoginCredentialsByEmail", e);
            return null;
        }
    }
}
