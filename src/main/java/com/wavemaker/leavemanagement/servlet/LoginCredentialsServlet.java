package com.wavemaker.leavemanagement.servlet;

import com.wavemaker.leavemanagement.model.Employee;
import com.wavemaker.leavemanagement.model.LeaveRequest;
import com.wavemaker.leavemanagement.model.LoginCredentials;
import com.wavemaker.leavemanagement.repositories.EmployeeRepository;
import com.wavemaker.leavemanagement.repositories.LeaveRequestRepository;
import com.wavemaker.leavemanagement.repositories.LoginCredentialsRepository;
import com.wavemaker.leavemanagement.repositories.impl.EmployeeRepositoryImpl;
import com.wavemaker.leavemanagement.repositories.impl.LeaveRequestRepositoryImpl;
import com.wavemaker.leavemanagement.repositories.impl.LoginCredentialsRepositoryImpl;
import com.wavemaker.leavemanagement.service.EmployeeService;
import com.wavemaker.leavemanagement.service.LeaveRequestService;
import com.wavemaker.leavemanagement.service.impl.EmployeeServiceImpl;
import com.wavemaker.leavemanagement.service.LoginCredentialsService;
import com.wavemaker.leavemanagement.service.impl.LeaveRequestServiceImpl;
import com.wavemaker.leavemanagement.service.impl.LoginCredentialsServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/loginCredentials")
public class LoginCredentialsServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(LoginCredentialsServlet.class.getName());
    private final LoginCredentialsRepository loginCredentialsRepository = new LoginCredentialsRepositoryImpl();
    private final LoginCredentialsService loginCredentialsService = new LoginCredentialsServiceImpl(loginCredentialsRepository);
    private final EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();
    private final EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);
    private final LeaveRequestRepository leaveRequestRepository = new LeaveRequestRepositoryImpl();
    private final LeaveRequestService leaveRequestService = new LeaveRequestServiceImpl(leaveRequestRepository) {
        @Override
        public List<LeaveRequest> getLeaveRequestsForEmployee(int employeeId) {
            return List.of();
        }
    };


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        LoginCredentials credentials = loginCredentialsService.getLoginCredentialsByEmail(email);
        if (credentials != null && credentials.getPassword().equals(password)) {
            Employee employee = employeeService.getEmployeeByEmail(email);
            if (employee != null) {
                request.getSession().setAttribute("employeeId", employee.getEmployeeId());
                request.getSession().setAttribute("email", email);
                request.getSession().setAttribute("employeeName", employee.getName());
                request.getSession().setAttribute("isManager", employee.getManagerId() == 0);
                if (employee.getManagerId() != 0) {
                    List<LeaveRequest> leaveRequest = leaveRequestService.getLeaveRequestsByManagerId(employee.getEmployeeId());
                    request.getSession().setAttribute("leaveRequest", leaveRequest);
                }
                response.sendRedirect("dashboard.html");
            } else {
                response.sendRedirect("error.html");
            }
        }
    }
    /*    @Override
        protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            try {
                String email = request.getParameter("email");
                String password = request.getParameter("password");

                if (email == null || password == null) {
                    throw new IllegalArgumentException("Email or password cannot be null");
                }

                LoginCredentials loginCredentials = new LoginCredentials(email, password);
                loginCredentialsService.updateLoginCredentials(loginCredentials);

                response.sendRedirect("success.html");
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.SEVERE, "Invalid input", e);
                response.sendRedirect("error.html");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error updating login credentials", e);
                response.sendRedirect("error.html");
            }
        }

        @Override
        protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            try {
                int loginCredentialsId = Integer.parseInt(request.getParameter("loginCredentialsId"));
                loginCredentialsService.deleteLoginCredentials(loginCredentialsId);
                response.sendRedirect("success.html");
            } catch (NumberFormatException e) {
                LOGGER.log(Level.SEVERE, "Invalid number format", e);
                response.sendRedirect("error.html");
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error deleting login credentials", e);
                response.sendRedirect("error.html");
            }
        }
    */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
           HttpSession httpSession = request.getSession();
           int employeeId = (int) httpSession.getAttribute("employeeId");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(employeeId);
            printWriter.flush();
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Invalid input", e);
            response.sendRedirect("error.html");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving login credentials or employee details", e);
            response.sendRedirect("error.html");
        }
    }
}

