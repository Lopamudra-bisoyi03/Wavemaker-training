package com.wavemaker.leavemanagement.model;

public class LoginCredentials {
    private int loginCredentialsId;
    private String email;
    private String password;

    // Constructor
    public LoginCredentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters and Setters
    public int getLoginCredentialsId() {
        return loginCredentialsId;
    }

    public void setLoginCredentialsId(int loginCredentialsId) {
        this.loginCredentialsId = loginCredentialsId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // toString
    @Override
    public String toString() {
        return "LoginCredentials{" +
                "loginCredentialsId=" + loginCredentialsId +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
