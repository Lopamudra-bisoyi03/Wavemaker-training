package com.wavemaker.leavemanagement.service;

import com.wavemaker.leavemanagement.model.LeaveRequest;
import com.wavemaker.leavemanagement.repositories.LeaveRequestRepository;

import java.util.List;

public interface LeaveRequestService {


    void addLeaveRequest(LeaveRequest leaveRequest);
    void updateLeaveRequest(LeaveRequest leaveRequest);
    void deleteLeaveRequest(int leaveRequestId);
    LeaveRequest getLeaveRequestById(int leaveRequestId);
    List<LeaveRequest> getLeaveRequestsForEmployee(int employeeId);
    List<LeaveRequest> getLeaveRequestsByEmployeeId(int employeeId);
    List<LeaveRequest> getLeaveRequestsByManagerId(int managerId);

    /**
     * Retrieves a list of leave requests for a specific manager.
     * @param managerId The ID of the manager whose team leaves are to be retrieved.
     * @return A list of LeaveRequest objects associated with the specified manager.
     */
    List<LeaveRequest> getLeaveRequestsForManager(int managerId);
}
