package com.wavemaker.leavemanagement.service.impl;

import com.wavemaker.leavemanagement.model.LeaveRequest;
import com.wavemaker.leavemanagement.repositories.LeaveRequestRepository;
import com.wavemaker.leavemanagement.service.LeaveRequestService;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class LeaveRequestServiceImpl implements LeaveRequestService {
    private static final Logger LOGGER = Logger.getLogger(LeaveRequestServiceImpl.class.getName());
    private final LeaveRequestRepository leaveRequestRepository;

    // Constructor should not have @Override annotation and should not handle exceptions
    public LeaveRequestServiceImpl(LeaveRequestRepository leaveRequestRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
    }

    @Override
    public void addLeaveRequest(LeaveRequest leaveRequest) {
        try {
            leaveRequestRepository.addLeaveRequest(leaveRequest);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in addLeaveRequest", e);
        }
    }

    @Override
    public void updateLeaveRequest(LeaveRequest leaveRequest) {
        // Implement if needed
    }

    @Override
    public void deleteLeaveRequest(int leaveRequestId) {
        // Implement if needed
    }

    @Override
    public LeaveRequest getLeaveRequestById(int leaveRequestId) {
        // Implement if needed
        return null;
    }

    @Override
    public List<LeaveRequest> getLeaveRequestsByEmployeeId(int employeeId) {
        try {
            return leaveRequestRepository.getLeaveRequestsByEmployeeId(employeeId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getLeaveRequestsByEmployeeId", e);
            return null;
        }
    }

    @Override
    public List<LeaveRequest> getLeaveRequestsByManagerId(int managerId) {
        try {
            return leaveRequestRepository.getLeaveRequestsByManagerId(managerId);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getLeaveRequestsByManagerId", e);
            return null;
        }
    }

    @Override
    public List<LeaveRequest> getLeaveRequestsForManager(int managerId) {
        try {
            // Fetch all leave requests
            List<LeaveRequest> allLeaveRequests = leaveRequestRepository.getAllLeaveRequests();

            // Filter leave requests based on managerId
            return allLeaveRequests.stream()
                    .filter(leaveRequest -> {
                        // Check if the leave request is assigned to an employee
                        int employeeId = leaveRequest.getEmployeeId();

                        // Fetch the employee's managerId (this might require another repository method)
                        // Assuming a method getEmployeeManagerId exists in the repository
                        int employeeManagerId = leaveRequestRepository.getEmployeeManagerId(employeeId);

                        // Return true if the employee's managerId matches the provided managerId
                        return employeeManagerId == managerId;
                    })
                    .toList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error in getLeaveRequestsForManager", e);
            return List.of(); // Return an empty list in case of an error
        }
    }
}
