package com.example.springstart.dto;

import com.example.springstart.domain.Todo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TodoResponseDto {

    @Schema(description = "할 일 고유 ID", example = "1")
    private Long id;

    @Schema(description = "할 일 제목", example ="운동하기")
    private String title;

    @Schema(description = "완료 여부", example ="true")
    private boolean completed;

    @Schema(description = "등록 일시", example = "22025-05-21T10:15:30")
    private LocalDateTime createdAt;

    @Schema(description = "수정 일시", example = "2025-05-21T11:00:00")
    private LocalDateTime updatedAt;

    @Schema(description = "작성자 이름", example = "jm4")
    private String username;

    public TodoResponseDto(Todo todo){
        this.id = todo.getId();
        this.title = todo.getTitle();
        this.completed = todo.isCompleted();
        this.createdAt = todo.getCreatedAt();
        this.updatedAt = todo.getUpdatedAt();
        this.username = todo.getUser().getUsername();
    }

    public static TodoResponseDto from(Todo todo){
        return new TodoResponseDto(todo);
    }
}
