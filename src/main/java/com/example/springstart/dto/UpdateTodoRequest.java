package com.example.springstart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateTodoRequest {
    private boolean completed;
}
