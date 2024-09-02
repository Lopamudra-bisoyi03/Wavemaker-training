package com.wavemaker.leavemanagement.repositories;

import com.wavemaker.leavemanagement.model.LeaveRequest;

import java.util.List;

public interface LeaveRequestRepository {
    void addLeaveRequest(LeaveRequest leaveRequest);

    void updateLeaveRequest(LeaveRequest leaveRequest);

    void deleteLeaveRequest(int leaveRequestId);

    LeaveRequest getLeaveRequestById(int leaveRequestId);

    List<LeaveRequest> getAllLeaveRequests();

    int getEmployeeManagerId(int employeeId);

    List<LeaveRequest> getLeaveRequestsByEmployeeId(int employeeId);

    List<LeaveRequest> getLeaveRequestsByManagerId(int managerId);

}
