package com.example.springstart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "할 일 등록 요청 DTO")
public class CreateTodoRequest {

    @NotBlank(message = "할 일 제목은 비어 있을 수 없습니다.")

    @Schema(description = "할 일 제목", example = "운동하기")
    private String title;

    @Schema(description = "완료 여부", example = "false")
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
