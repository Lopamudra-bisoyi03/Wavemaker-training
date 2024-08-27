package com.todoapp.repository;

import com.todoapp.models.Todo;

import java.util.List;
import java.util.Optional;

public interface TodoRepository {
    void addTodo(Todo todo);
    Optional<Todo> getTodoById(int id);
    List<Todo> getAllTodos(int id);
    boolean updateTodo(Todo todo);
    void deleteTodo(int id);

    boolean deleteTodoById(int id);

}
