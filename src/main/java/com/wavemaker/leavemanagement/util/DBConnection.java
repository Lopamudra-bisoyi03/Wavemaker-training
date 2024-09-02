package com.wavemaker.leavemanagement.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DBConnection {

    private static final Logger log = Logger.getLogger(DBConnection.class.getName());
    private static final String DB_URL = "jdbc:mysql://localhost:3306/LEAVE_MANAGEMENT";
    private static final String USER = "root";
    private static final String PASSWORD = "87707@";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            log.severe("MySQL JDBC Driver not found. " + e.getMessage());
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
}
