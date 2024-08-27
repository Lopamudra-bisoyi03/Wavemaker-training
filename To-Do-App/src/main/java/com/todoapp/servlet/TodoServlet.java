package com.todoapp.servlet;

import com.todoapp.models.Todo;
import com.todoapp.models.User;
import com.todoapp.services.TodoService;
import com.todoapp.services.impl.TodoServiceImpl;
import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Optional;

@WebServlet("/todos")
public class TodoServlet extends HttpServlet {
    private final TodoService todoService = new TodoServiceImpl();
    private final Gson gson = new Gson();  // Replace ObjectMapper with Gson

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            return;
        }

        User user = (User) session.getAttribute("user");
        String idParam = request.getParameter("id");

        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            Optional<Todo> todo = todoService.getTodoById(id, user.getId());
            if (todo.isPresent()) {
                response.setContentType("application/json");
                gson.toJson(todo.get(), response.getWriter());
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Todo not found");
            }
        } else {
            List<Todo> todos = todoService.getAllTodos(user.getId());
            String jsonResponse = gson.toJson(todos);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter printWriter = response.getWriter();
            printWriter.print(jsonResponse);
            printWriter.flush();

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            return;
        }

        User user = (User) session.getAttribute("user");
        Todo todo = gson.fromJson(request.getReader(), Todo.class);
        todo.setUserId(user.getId());
        todoService.addTodo(todo);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            return;
        }

        User user = (User) session.getAttribute("user");
        Todo todo = gson.fromJson(request.getReader(), Todo.class);
        todo.setUserId(user.getId());
        boolean updated = todoService.updateTodo(todo);
        if (updated) {
            response.setStatus(HttpServletResponse.SC_OK);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Todo not found");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not logged in");
            return;
        }

        User user = (User) session.getAttribute("user");
        String idParam = request.getParameter("id");
        if (idParam != null) {
            int id = Integer.parseInt(idParam);
            boolean deleted = todoService.deleteTodoById(id, user.getId());
            if (deleted) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Todo not found");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID parameter is missing");
        }
    }
}
