package com.wavemaker.leavemanagement.repositories.impl;

import com.wavemaker.leavemanagement.repositories.EmployeeRepository;
import com.wavemaker.leavemanagement.model.Employee;
import com.wavemaker.leavemanagement.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeRepositoryImpl implements EmployeeRepository {
    static final Logger LOGGER = Logger.getLogger(EmployeeRepositoryImpl.class.getName());
    private static final String INSERT_EMPLOYEE_SQL = "INSERT INTO EMPLOYEE (NAME, EMAIL, PHONE, DOB, MANAGER_ID) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_EMPLOYEE_SQL = "UPDATE EMPLOYEE SET NAME = ?, EMAIL = ?, PHONE = ?, DOB = ?, MANAGER_ID = ? WHERE EMPLOYEE_ID = ?";
    private static final String DELETE_EMPLOYEE_SQL = "DELETE FROM EMPLOYEE WHERE EMPLOYEE_ID = ?";
    private static final String SELECT_EMPLOYEE_BY_ID = "SELECT * FROM EMPLOYEE WHERE EMPLOYEE_ID = ?";
    private static final String SELECT_EMPLOYEE_BY_EMAIL = "SELECT * FROM EMPLOYEE WHERE EMAIL = ?";
    private static final String SELECT_ALL_EMPLOYEES = "SELECT * FROM EMPLOYEE";

    @Override
    public void addEmployee(Employee employee) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_EMPLOYEE_SQL)) {
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getEmail());
            preparedStatement.setString(3, employee.getPhone());
            preparedStatement.setString(4, employee.getDob());
            preparedStatement.setInt(5, employee.getManagerId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error adding employee: " + employee, e);
        }
    }

    @Override
    public void updateEmployee(Employee employee) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EMPLOYEE_SQL)) {
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getEmail());
            preparedStatement.setString(3, employee.getPhone());
            preparedStatement.setString(4, employee.getDob());
            preparedStatement.setInt(5, employee.getManagerId());
            preparedStatement.setInt(6, employee.getEmployeeId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating employee: " + employee, e);
        }
    }

    @Override
    public void deleteEmployee(int employeeId) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_EMPLOYEE_SQL)) {
            preparedStatement.setInt(1, employeeId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting employee with ID: " + employeeId, e);
        }
    }

    @Override
    public Employee getEmployeeById(int employeeId) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EMPLOYEE_BY_ID)) {
            preparedStatement.setInt(1, employeeId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToEmployee(resultSet);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving employee by ID: " + employeeId, e);
        }
        return null;
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_EMPLOYEE_BY_EMAIL)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapRowToEmployee(resultSet);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving employee by email: " + email, e);
        }
        return null;
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_EMPLOYEES);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                employees.add(mapRowToEmployee(resultSet));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all employees", e);
        }
        return employees;
    }

    private Employee mapRowToEmployee(ResultSet resultSet) throws SQLException {
        return new Employee(
                resultSet.getString("NAME"),
                resultSet.getString("EMAIL"),
                resultSet.getString("PHONE"),
                resultSet.getString("DOB"),
                resultSet.getInt("MANAGER_ID")
        );
    }
}
