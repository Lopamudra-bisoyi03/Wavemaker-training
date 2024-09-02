package com.wavemaker.leavemanagement.service.impl;

import com.wavemaker.leavemanagement.model.Employee;
import com.wavemaker.leavemanagement.repositories.EmployeeRepository;
import com.wavemaker.leavemanagement.service.EmployeeService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EmployeeServiceImpl implements EmployeeService {
    private static final Logger LOGGER = Logger.getLogger(EmployeeServiceImpl.class.getName());
    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void addEmployee(Employee employee) {
        try {
            employeeRepository.addEmployee(employee);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in addEmployee", e);
        }
    }

    @Override
    public void updateEmployee(Employee employee) {
        try {
            employeeRepository.updateEmployee(employee);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in updateEmployee", e);
        }
    }

    @Override
    public void deleteEmployeeById(int employeeId) {
        try {
            employeeRepository.deleteEmployee(employeeId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in deleteEmployeeById", e);
        }
    }

    @Override
    public List<Employee> getAllEmployees() {
        try {
            return employeeRepository.getAllEmployees();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getAllEmployees", e);
            return null;
        }
    }

    @Override
    public Employee getEmployeeById(int employeeId) {
        return employeeRepository.getEmployeeById(employeeId);
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.getEmployeeByEmail(email);
    }
}
