package com.todoapp.services.impl;

import com.todoapp.models.User;
import com.todoapp.services.UserService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserServiceImpl implements UserService {
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/todo_app";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "87707@";

    public UserServiceImpl() {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }

    }
    // Establish a database connection
    private Connection connect() throws SQLException {

        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    @Override
    public void registerUser(User user) {
        String sql = "INSERT INTO USERS (EMAIL, PASSWORD, NAME) VALUES (?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters for the query
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getUsername());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Handle errors properly
        }
    }

    @Override
    public User loginUser(String email, String password) {
        User user = null;
        String sql = "SELECT * FROM USERS WHERE EMAIL = ? AND PASSWORD = ?";

        try {
            Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            // Set the parameters for the query
            pstmt.setString(1, email);
            pstmt.setString(2, password); // Ensure you use hashed passwords in production

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Assuming User class has a constructor that accepts parameters
                    user = new User(
                            rs.getInt("USER_ID"),
                            rs.getString("EMAIL"),
                            rs.getString("PASSWORD"),
                            rs.getString("USERNAME") // Adjust field names as needed
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle errors properly
        }

        return user;
    }
}
