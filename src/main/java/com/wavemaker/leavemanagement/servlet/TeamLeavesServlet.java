package com.wavemaker.leavemanagement.servlet;

import com.wavemaker.leavemanagement.model.LeaveRequest;
import com.wavemaker.leavemanagement.service.LeaveRequestService;
import com.wavemaker.leavemanagement.service.impl.LeaveRequestServiceImpl;
import com.wavemaker.leavemanagement.repositories.LeaveRequestRepository;
import com.wavemaker.leavemanagement.repositories.impl.LeaveRequestRepositoryImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/fetchTeamLeaves")
public class TeamLeavesServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(TeamLeavesServlet.class.getName());
    private final LeaveRequestRepository leaveRequestRepository = new LeaveRequestRepositoryImpl();
    private final LeaveRequestService leaveRequestService = new LeaveRequestServiceImpl(leaveRequestRepository) {
        @Override
        public List<LeaveRequest> getLeaveRequestsForEmployee(int employeeId) {
            return List.of();
        }
    };

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Retrieve and validate managerId from the request
        String managerIdParam = request.getParameter("managerId");
        if (managerIdParam == null || managerIdParam.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing or empty managerId parameter");
            return;
        }

        int managerId;
        try {
            managerId = Integer.parseInt(managerIdParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid managerId format");
            return;
        }

        JSONArray jsonArray = new JSONArray();

        try {
            List<LeaveRequest> leaveRequests = leaveRequestService.getLeaveRequestsForManager(managerId);
            for (LeaveRequest leaveRequest : leaveRequests) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("leaveRequestId", leaveRequest.getLeaveRequestId());
                jsonObject.put("employeeId", leaveRequest.getEmployeeId());
                jsonObject.put("leaveType", leaveRequest.getLeaveType());
                jsonObject.put("fromDate", leaveRequest.getFromDate().toString());
                jsonObject.put("toDate", leaveRequest.getToDate().toString());
                jsonObject.put("reason", leaveRequest.getReason());
                jsonObject.put("status", leaveRequest.getStatus());
                jsonObject.put("createdAt", leaveRequest.getCreatedAt().toString());
                jsonArray.put(jsonObject);
            }

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(jsonArray.toString());
            out.flush();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error fetching team leaves", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to fetch team leaves: " + e.getMessage());
        }
    }
}
