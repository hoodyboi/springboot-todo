package com.example.springstart.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateTodoRequest {

    @NotBlank(message = "할 일 제목은 비어 있을 수 없습니다.")
    private String title;
    private boolean completed;

    public CreateTodoRequest() {}

    public CreateTodoRequest(String title, boolean completed){
        this.title = title;
        this.completed = completed;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public boolean isCompleted(){
        return completed;
    }

    public void setCompleted(boolean completed){
        this.completed = completed;
    }
}
