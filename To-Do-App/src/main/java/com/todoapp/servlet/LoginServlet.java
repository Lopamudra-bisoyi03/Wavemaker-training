package com.todoapp.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todoapp.models.User;
import com.todoapp.services.UserService;
import com.todoapp.services.impl.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final UserService userService = new UserServiceImpl();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        // Validate the user credentials
        User user = userService.loginUser(email, password);

        if (user != null) {
            if (user.getUsername() == null) {
                // Generate a new username if it doesn't exist
                String generatedUsername = generateUsername(user.getEmail());
                user.setUsername(generatedUsername);
                userService.registerUser(user);
            }

            // Create a session for the user
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            // Send a JSON response with user information
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.sendRedirect("index.html");
        } else {
            // Send an unauthorized error response
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid email or password");
        }
    }

    private String generateUsername(String email) {
        return email.split("@")[0] + "_" + System.currentTimeMillis();
    }
}
