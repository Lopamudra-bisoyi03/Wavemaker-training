package com.wavemaker.leavemanagement.model;

import java.util.Date;

public class LeaveRequest {
    private int leaveRequestId;
    private int employeeId;
    private String leaveType;
    private Date fromDate;
    private Date toDate;
    private String reason;
    private String status;
    private Date createdAt;
    private String approvedBy;
    private Date approvedAt;

    // Constructor
    public LeaveRequest(int employeeId, String leaveType, Date fromDate, Date toDate, String reason, String status, Date createdAt, String approvedBy, Date approvedAt) {
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.reason = reason;
        this.status = status;
        this.createdAt = createdAt;
        this.approvedBy = approvedBy;
        this.approvedAt = approvedAt;
    }

    // Getters and Setters
    public int getLeaveRequestId() {
        return leaveRequestId;
    }

    public void setLeaveRequestId(int leaveRequestId) {
        this.leaveRequestId = leaveRequestId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Date getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(Date approvedAt) {
        this.approvedAt = approvedAt;
    }

    // toString
    @Override
    public String toString() {
        return "LeaveRequest{" +
                "leaveRequestId=" + leaveRequestId +
                ", employeeId=" + employeeId +
                ", leaveType='" + leaveType + '\'' +
                ", fromDate=" + fromDate +
                ", toDate=" + toDate +
                ", reason='" + reason + '\'' +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", approvedBy='" + approvedBy + '\'' +
                ", approvedAt=" + approvedAt +
                '}';
    }
}
