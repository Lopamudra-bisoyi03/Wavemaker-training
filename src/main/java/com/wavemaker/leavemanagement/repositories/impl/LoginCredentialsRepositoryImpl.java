package com.wavemaker.leavemanagement.repositories.impl;

import com.wavemaker.leavemanagement.model.LoginCredentials;
import com.wavemaker.leavemanagement.repositories.LoginCredentialsRepository;
import com.wavemaker.leavemanagement.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.wavemaker.leavemanagement.repositories.impl.EmployeeRepositoryImpl.LOGGER;

public class LoginCredentialsRepositoryImpl implements LoginCredentialsRepository {
    private static final Logger logger = Logger.getLogger(LoginCredentialsRepositoryImpl.class.getName());
    private static final String INSERT_LOGIN_CREDENTIALS_SQL = "INSERT INTO LOGIN_CREDENTIALS (EMAIL, PASSWORD) VALUES (?, ?)";
    private static final String UPDATE_LOGIN_CREDENTIALS_SQL = "UPDATE LOGIN_CREDENTIALS SET EMAIL = ?, PASSWORD = ? WHERE LOGIN_CREDENTIALS_ID = ?";
    private static final String SELECT_LOGIN_CREDENTIALS_BY_EMAIL = "SELECT * FROM LOGIN_CREDENTIALS WHERE EMAIL = ?";

    @Override
    public void addLoginCredentials(LoginCredentials loginCredentials) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_LOGIN_CREDENTIALS_SQL)) {
            preparedStatement.setString(1, loginCredentials.getEmail());
            preparedStatement.setString(2, loginCredentials.getPassword());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding login credentials", e);
        }
    }


    @Override
    public void updateLoginCredentials(LoginCredentials loginCredentials) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_LOGIN_CREDENTIALS_SQL)) {
            preparedStatement.setString(1, loginCredentials.getEmail());
            preparedStatement.setString(2, loginCredentials.getPassword());
            preparedStatement.setInt(3, loginCredentials.getLoginCredentialsId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating login credentials", e);
        }
    }

    @Override
    public LoginCredentials getLoginCredentialsByEmail(String email) {
        LoginCredentials loginCredentials = null;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LOGIN_CREDENTIALS_BY_EMAIL)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                loginCredentials = new LoginCredentials(
                        resultSet.getString("EMAIL"),
                        resultSet.getString("PASSWORD")
                );
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving login credentials", e);
        }
        return loginCredentials;
    }
}
