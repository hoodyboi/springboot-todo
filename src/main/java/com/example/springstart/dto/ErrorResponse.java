package com.example.springstart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int statusCode;
    private String error;
    private Object message;
    private String code;
}
