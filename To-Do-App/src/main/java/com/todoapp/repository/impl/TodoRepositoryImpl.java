package com.todoapp.repository.impl;

import com.todoapp.models.Todo;
import com.todoapp.repository.TodoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TodoRepositoryImpl implements TodoRepository {

    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/todo_app";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "87707@";

    private Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void addTodo(Todo todo) {
        String sql = "INSERT INTO TODOS (TITLE, PRIORITY, DESCRIPTION, IS_DONE, CREATED_BY, DUE_DATE) VALUES (?,?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, todo.getTitle());
            pstmt.setString(2, todo.getPriority());
            pstmt.setString(3, todo.getDescription());
            pstmt.setBoolean(4, todo.isDone());
            pstmt.setString(5, todo.getCreatedBy());
            pstmt.setTimestamp(6,todo.getDueDate());



            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Optional<Todo> getTodoById(int id) {
        String sql = "SELECT * FROM TODOS WHERE ID = ?";
        Todo todo = null;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                todo = mapResultSetToTodoTask(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(todo);
    }

    @Override
    public List<Todo> getAllTodos(int userId) {
        List<Todo> todos = new ArrayList<>();
        String sql = "SELECT * FROM TODOS WHERE USER_ID = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    todos.add(mapResultSetToTodoTask(rs));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return todos;
    }

    @Override
    public boolean updateTodo(Todo todo) {
        String sql = "UPDATE TODOS SET TITLE = ?, PRIORITY = ?, DESCRIPTION = ?, IS_DONE = ? WHERE ID = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, todo.getTitle());
            pstmt.setString(2, todo.getPriority());
            pstmt.setString(3, todo.getDescription());
            pstmt.setBoolean(4, todo.isDone());
            pstmt.setInt(5, todo.getId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void deleteTodo(int id) {
        String sql = "DELETE FROM TODOS WHERE ID = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean deleteTodoById(int id) {
        // This method seems redundant since deleteTodo(int id) already performs deletion.
        // Consider removing it if not used.
        return false;
    }

    private Todo mapResultSetToTodoTask(ResultSet rs) throws SQLException {
        return new Todo(
                rs.getInt("ID"),
                rs.getString("TITLE"),
                rs.getString("PRIORITY"),
                rs.getString("DESCRIPTION"),
                rs.getTimestamp("DUE_DATE"),
                rs.getInt("USER_ID"),
                rs.getBoolean("IS_DONE"),
                rs.getTimestamp("CREATED_AT"),
                rs.getString("CREATED_BY")
        );
    }
}
