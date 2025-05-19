package com.example.springstart.dto;

public class UpdateTodoRequest {
    private boolean completed;
    public boolean isCompleted(){
        return completed;
    }

    public void setCompleted(boolean completed){
        this.completed = completed;
    }
}
