package com.todoapp.services.impl;

import com.todoapp.models.Todo;
import com.todoapp.repository.TodoRepository;
import com.todoapp.repository.impl.TodoRepositoryImpl;
import com.todoapp.services.TodoService;

import java.util.List;
import java.util.Optional;

public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository = new TodoRepositoryImpl();

    @Override
    public void createTodo(Todo todo) {

    }

    @Override
    public Optional<Todo> getTodoById(int id, int userId) {
        return todoRepository.getTodoById(id);
    }

    @Override
    public List<Todo> getAllTodos(int id) {
        return todoRepository.getAllTodos(id);
    }

    @Override
    public void addTodo(Todo todo) {
        todoRepository.addTodo(todo);
    }

    @Override
    public boolean updateTodo(Todo todo) {
        return todoRepository.updateTodo(todo);
    }

    @Override
    public boolean deleteTodoById(int id, int userId) {
        return todoRepository.deleteTodoById(id);
    }

}