package com.todoapp.services;

import com.todoapp.models.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoService {
    void createTodo(Todo todo);

    Optional<Todo> getTodoById(int id, int userId);
    List<Todo> getAllTodos(int userid);
    void addTodo(Todo todo);
    boolean updateTodo(Todo todo);
    boolean deleteTodoById(int id, int userId);

}
