package com.wavemaker.leavemanagement.repositories;

import com.wavemaker.leavemanagement.model.Employee;

import java.util.List;

public interface EmployeeRepository {
    void addEmployee(Employee employee);

    void updateEmployee(Employee employee);

    void deleteEmployee(int employeeId);

    Employee getEmployeeById(int employeeId);

    Employee getEmployeeByEmail(String email);
    List<Employee> getAllEmployees();
}



