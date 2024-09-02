package com.wavemaker.leavemanagement.repositories.impl;

import java.sql.SQLException;
import java.util.logging.Level;

import com.wavemaker.leavemanagement.model.LeaveRequest;
import com.wavemaker.leavemanagement.repositories.LeaveRequestRepository;
import com.wavemaker.leavemanagement.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class LeaveRequestRepositoryImpl implements LeaveRequestRepository {
    private static final Logger LOGGER = Logger.getLogger(LeaveRequestRepositoryImpl.class.getName());
    private static final String INSERT_LEAVE_REQUEST_SQL = "INSERT INTO LEAVE_REQUEST (EMPLOYEE_ID, LEAVE_TYPE, FROM_DATE, TO_DATE, REASON, STATUS, CREATED_AT, APPROVED_BY, APPROVED_AT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_LEAVE_REQUEST_SQL = "UPDATE LEAVE_REQUEST SET EMPLOYEE_ID = ?, LEAVE_TYPE = ?, FROM_DATE = ?, TO_DATE = ?, REASON = ?, STATUS = ?, CREATED_AT = ?, APPROVED_BY = ?, APPROVED_AT = ? WHERE LEAVE_REQUEST_ID = ?";
    private static final String DELETE_LEAVE_REQUEST_SQL = "DELETE FROM LEAVE_REQUEST WHERE LEAVE_REQUEST_ID = ?";
    private static final String SELECT_LEAVE_REQUEST_BY_ID = "SELECT * FROM LEAVE_REQUEST WHERE LEAVE_REQUEST_ID = ?";
    private static final String SELECT_LEAVE_REQUESTS_BY_EMPLOYEE_ID = "SELECT * FROM LEAVE_REQUEST WHERE EMPLOYEE_ID = ?";
    private static final String SELECT_LEAVE_REQUESTS_BY_MANAGER_ID = "SELECT lr.LEAVE_REQUEST_ID, " +
            "       lr.EMPLOYEE_ID, " +
            "       lr.LEAVE_TYPE, " +
            "       lr.FROM_DATE, " +
            "       lr.TO_DATE, " +
            "       lr.REASON, " +
            "       lr.STATUS, " +
            "       lr.CREATED_AT, " +
            "       lr.APPROVED_BY, " +
            "       lr.APPROVED_AT " +
            "FROM LEAVE_REQUEST lr " +
            "JOIN EMPLOYEE e ON lr.EMPLOYEE_ID = e.EMPLOYEE_ID " +
            "WHERE e.MANAGER_ID = ?";

    @Override
    public void addLeaveRequest(LeaveRequest leaveRequest) {
        String sql = "INSERT INTO LEAVE_REQUEST (EMPLOYEE_ID, LEAVE_TYPE, FROM_DATE, TO_DATE, REASON, STATUS, CREATED_AT, APPROVED_BY, APPROVED_AT) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, leaveRequest.getEmployeeId());
            preparedStatement.setString(2, leaveRequest.getLeaveType());
            preparedStatement.setDate(3, new java.sql.Date(leaveRequest.getFromDate().getTime()));
            preparedStatement.setDate(4, new java.sql.Date(leaveRequest.getToDate().getTime()));
            preparedStatement.setString(5, leaveRequest.getReason());
            preparedStatement.setString(6, leaveRequest.getStatus());
            preparedStatement.setTimestamp(7, new Timestamp(leaveRequest.getCreatedAt().getTime()));
            preparedStatement.setObject(8, leaveRequest.getApprovedBy(), Types.INTEGER);
            preparedStatement.setTimestamp(9, leaveRequest.getApprovedAt() != null ? new Timestamp(leaveRequest.getApprovedAt().getTime()) : null);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("Failed to insert leave request, no rows affected.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding leave request", e);
        }
    }


    @Override
    public void updateLeaveRequest(LeaveRequest leaveRequest) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_LEAVE_REQUEST_SQL)) {
            preparedStatement.setInt(1, leaveRequest.getEmployeeId());
            preparedStatement.setString(2, leaveRequest.getLeaveType());
            preparedStatement.setDate(3, new java.sql.Date(leaveRequest.getFromDate().getTime()));
            preparedStatement.setDate(4, new java.sql.Date(leaveRequest.getToDate().getTime()));
            preparedStatement.setString(5, leaveRequest.getReason());
            preparedStatement.setString(6, leaveRequest.getStatus());
            preparedStatement.setTimestamp(7, new Timestamp(leaveRequest.getCreatedAt().getTime()));
            preparedStatement.setObject(8, leaveRequest.getApprovedBy(), Types.INTEGER);
            preparedStatement.setTimestamp(9, new Timestamp(leaveRequest.getApprovedAt() != null ? leaveRequest.getApprovedAt().getTime() : null));
            preparedStatement.setInt(10, leaveRequest.getLeaveRequestId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating leave request", e);
        }
    }

    @Override
    public void deleteLeaveRequest(int leaveRequestId) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_LEAVE_REQUEST_SQL)) {
            preparedStatement.setInt(1, leaveRequestId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting leave request", e);
        }
    }

    @Override
    public List<LeaveRequest> getAllLeaveRequests() {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        String selectAllLeaveRequestsSql = "SELECT * FROM LEAVE_REQUEST";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(selectAllLeaveRequestsSql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                LeaveRequest leaveRequest = new LeaveRequest(
                        resultSet.getInt("EMPLOYEE_ID"),
                        resultSet.getString("LEAVE_TYPE"),
                        resultSet.getDate("FROM_DATE"),
                        resultSet.getDate("TO_DATE"),
                        resultSet.getString("REASON"),
                        resultSet.getString("STATUS"),
                        resultSet.getTimestamp("CREATED_AT"),
                        resultSet.getString("APPROVED_BY"),
                        resultSet.getTimestamp("APPROVED_AT")
                );
                leaveRequests.add(leaveRequest);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all leave requests", e);
        }
        return leaveRequests;
    }


    @Override
    public LeaveRequest getLeaveRequestById(int leaveRequestId) {
        LeaveRequest leaveRequest = null;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LEAVE_REQUEST_BY_ID)) {
            preparedStatement.setInt(1, leaveRequestId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                leaveRequest = new LeaveRequest(
                        resultSet.getInt("EMPLOYEE_ID"),
                        resultSet.getString("LEAVE_TYPE"),
                        resultSet.getDate("FROM_DATE"),
                        resultSet.getDate("TO_DATE"),
                        resultSet.getString("REASON"),
                        resultSet.getString("STATUS"),
                        resultSet.getTimestamp("CREATED_AT"),
                        resultSet.getString("APPROVED_BY"),
                        resultSet.getTimestamp("APPROVED_AT")
                );
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving leave request", e);
        }
        return leaveRequest;
    }

    @Override
    public int getEmployeeManagerId(int employeeId) {
        return 0;
    }

    @Override
    public List<LeaveRequest> getLeaveRequestsByEmployeeId(int employeeId) {
        LeaveRequestRepositoryImpl leaveRequestService = new LeaveRequestRepositoryImpl();
        List<LeaveRequest> leaveRequests = leaveRequestService.getLeaveRequestsByEmployeeId(employeeId);
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LEAVE_REQUESTS_BY_EMPLOYEE_ID)) {
            preparedStatement.setInt(1, employeeId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                LeaveRequest leaveRequest = new LeaveRequest(

                        resultSet.getInt("EMPLOYEE_ID"),
                        resultSet.getString("LEAVE_TYPE"),
                        resultSet.getDate("FROM_DATE"),
                        resultSet.getDate("TO_DATE"),
                        resultSet.getString("REASON"),
                        resultSet.getString("STATUS"),
                        resultSet.getTimestamp("CREATED_AT"),
                        resultSet.getString("APPROVED_BY"),
                        resultSet.getTimestamp("APPROVED_AT")
                );
                leaveRequests.add(leaveRequest);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving leave requests", e);
        }
        return leaveRequests;
    }

    @Override
    public List<LeaveRequest> getLeaveRequestsByManagerId(int managerId) {
        List<LeaveRequest> leaveRequests = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_LEAVE_REQUESTS_BY_MANAGER_ID)) {
            preparedStatement.setInt(1, managerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                LeaveRequest leaveRequest = new LeaveRequest(
                        resultSet.getInt("EMPLOYEE_ID"),
                        resultSet.getString("LEAVE_TYPE"),
                        resultSet.getDate("FROM_DATE"),
                        resultSet.getDate("TO_DATE"),
                        resultSet.getString("REASON"),
                        resultSet.getString("STATUS"),
                        resultSet.getTimestamp("CREATED_AT"),
                        resultSet.getString("APPROVED_BY"),
                        resultSet.getTimestamp("APPROVED_AT")
                );
                leaveRequests.add(leaveRequest);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving leave requests by manager ID", e);
        }
        return leaveRequests;
    }

}

