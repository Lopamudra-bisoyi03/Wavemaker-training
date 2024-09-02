package com.wavemaker.leavemanagement.service;

import com.wavemaker.leavemanagement.model.Employee;

import java.util.List;

public interface EmployeeService {

    void addEmployee(Employee employee);

    void updateEmployee(Employee employee);

    void deleteEmployeeById(int employeeId);

    List<Employee> getAllEmployees();

    Employee getEmployeeById(int employeeId);

    Employee getEmployeeByEmail(String email);
}
