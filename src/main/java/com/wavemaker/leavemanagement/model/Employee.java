package com.wavemaker.leavemanagement.model;


public class Employee {
    private int employeeId;
    private String name;
    private String email;
    private String phone;
    private String dob;
    private int managerId;

    // Constructor
    public Employee(String name, String email, String phone, String dob, int managerId) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.managerId = managerId;
    }

    // Getters and Setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", dob=" + dob +
                ", managerId=" + managerId +
                '}';
    }
}
