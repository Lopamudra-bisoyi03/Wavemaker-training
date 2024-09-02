package com.wavemaker.leavemanagement.servlet;

import com.wavemaker.leavemanagement.model.Employee;
import com.wavemaker.leavemanagement.repositories.EmployeeRepository;
import com.wavemaker.leavemanagement.repositories.impl.EmployeeRepositoryImpl;
import com.wavemaker.leavemanagement.service.EmployeeService;
import com.wavemaker.leavemanagement.service.impl.EmployeeServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/employee")
public class EmployeeServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(EmployeeServlet.class.getName());
    private final EmployeeRepository employeeRepository = new EmployeeRepositoryImpl();
    private final EmployeeService employeeService = new EmployeeServiceImpl(employeeRepository);

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String name = request.getParameter("Name");
            String email = request.getParameter("Email");
            String phone = request.getParameter("Phone");
            String dob = request.getParameter("DOB");
            int managerId = Integer.parseInt(request.getParameter("ManagerID"));

            Employee employee = new Employee(name, email, phone, dob, managerId);
            employeeService.addEmployee(employee);

            response.sendRedirect("success.html");
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid number format", e);
            response.sendRedirect("error.html");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error adding employee", e);
            response.sendRedirect("error.html");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int employeeId = Integer.parseInt(request.getParameter("employeeId"));
            String name = request.getParameter("Name");
            String email = request.getParameter("Email");
            String phone = request.getParameter("Phone");
            String dob = request.getParameter("DOB");
            int managerId = Integer.parseInt(request.getParameter("ManagerID"));

            Employee employee = new Employee(name, email, phone, dob, managerId);
            employee.setEmployeeId(employeeId);
            employeeService.updateEmployee(employee);

            response.sendRedirect("success.html");
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid number format", e);
            response.sendRedirect("error.html");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error updating employee", e);
            response.sendRedirect("error.html");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int employeeId = Integer.parseInt(request.getParameter("employeeId"));
            employeeService.deleteEmployeeById(employeeId);
            response.sendRedirect("success.html");
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid number format", e);
            response.sendRedirect("error.html");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deleting employee", e);
            response.sendRedirect("error.html");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String employeeIdParam = request.getParameter("employeeId");
            if (employeeIdParam == null || employeeIdParam.isEmpty()) {
                throw new NumberFormatException("Employee ID is missing or empty");
            }

            int employeeId = Integer.parseInt(employeeIdParam);
            Employee employee = employeeService.getEmployeeById(employeeId);

            if (employee == null) {
                throw new ServletException("Employee not found");
            }

            request.setAttribute("employee", employee);
            request.getRequestDispatcher("employeeDetails.jsp").forward(request, response);
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid number format", e);
            response.sendRedirect("error.html");
        } catch (ServletException e) {
            LOGGER.log(Level.SEVERE, "Employee not found", e);
            response.sendRedirect("error.html");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving employee", e);
            response.sendRedirect("error.html");
        }
    }
}
