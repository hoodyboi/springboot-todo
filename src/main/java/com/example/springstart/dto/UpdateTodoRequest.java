package com.example.springstart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(description = "할 일 완료 상태 변경 요청 DTO")
public class UpdateTodoRequest {

    @Schema(description = "할 일 완료 여부", example = "true")
    private boolean completed;
}
