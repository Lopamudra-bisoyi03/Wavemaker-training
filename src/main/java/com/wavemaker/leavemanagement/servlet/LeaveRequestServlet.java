package com.wavemaker.leavemanagement.servlet;

import com.wavemaker.leavemanagement.model.Employee;
import com.wavemaker.leavemanagement.model.LeaveRequest;
import com.wavemaker.leavemanagement.repositories.EmployeeRepository;
import com.wavemaker.leavemanagement.repositories.LeaveRequestRepository;
import com.wavemaker.leavemanagement.repositories.impl.EmployeeRepositoryImpl;
import com.wavemaker.leavemanagement.repositories.impl.LeaveRequestRepositoryImpl;
import com.wavemaker.leavemanagement.service.EmployeeService;
import com.wavemaker.leavemanagement.service.LeaveRequestService;
import com.wavemaker.leavemanagement.service.impl.EmployeeServiceImpl;
import com.wavemaker.leavemanagement.service.impl.LeaveRequestServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;


import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.sql.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/leaveRequest")
public class LeaveRequestServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LeaveRequestServlet.class.getName());
    private final LeaveRequestRepository leaveRequestRepository = new LeaveRequestRepositoryImpl();
    private final LeaveRequestService leaveRequestService = new LeaveRequestServiceImpl(leaveRequestRepository) {
        @Override
        public List<LeaveRequest> getLeaveRequestsForEmployee(int employeeId) {
            return List.of();
        }
    };
    EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();
    EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User is not logged in");
            return;
        }

        String email = (String) session.getAttribute("email");
        if (email == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "No email found in session");
            return;
        }

        EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();
        EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);
        JSONArray jsonArray = new JSONArray();

        try {
            Employee employee = employeeService.getEmployeeByEmail(email);
            if (employee == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Employee not found");
                return;
            }

            int employeeId = employee.getEmployeeId();
            List<LeaveRequest> leaveRequests = leaveRequestService.getLeaveRequestsByEmployeeId(employeeId);
            for (LeaveRequest leaveRequest : leaveRequests) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("leaveRequestId", leaveRequest.getLeaveRequestId());
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
            LOGGER.log(Level.SEVERE, "Error fetching leave requests", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to fetch leave requests: " + e.getMessage());
        }
    }
}
