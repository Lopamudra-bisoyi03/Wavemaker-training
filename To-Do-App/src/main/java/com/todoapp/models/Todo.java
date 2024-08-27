package com.todoapp.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Todo {
    private int id;
    private String title;
    private String priority;
    private String description;
    private Timestamp dueDate;
    private int userId;
    private boolean isDone;
    private Timestamp createdAt;
    private String createdBy;

    // constructor
    public Todo() {
    }

    public Todo(int id, String title, String priority,String description, Timestamp dueDate, int userId, boolean isDone, Timestamp createdAt, String createdBy) {
        this.id = id;
        this.title = title;
        this.priority = priority;
        this.description = description;
        this.dueDate = dueDate;
        this.userId = userId;
        this.isDone = isDone;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
    }



    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getPriority() {
        return priority;
    }
    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }
    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int id) {
        this.id = id;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean isDone) {
        this.isDone = isDone;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "Todo{" + "id=" + id + ", title='" + title + '\'' + ", description='" + description + '\'' + ", isDone=" + isDone + ", createdAt=" + createdAt + ", createdBy='" + createdBy + '\'' + '}';
    }

}