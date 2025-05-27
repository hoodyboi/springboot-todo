package com.example.springstart.dto;

import com.example.springstart.domain.Todo;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TodoResponseDto {
    private Long id;
    private String title;
    private boolean completed;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private String username;

    public TodoResponseDto(Todo todo){
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.completed = todo.isCompleted();
        this.createdAt = todo.getCreatedAt();
        this.updateAt = todo.getUpdatedAt();
        this.username = todo.getUser().getUsername();
    }
}
