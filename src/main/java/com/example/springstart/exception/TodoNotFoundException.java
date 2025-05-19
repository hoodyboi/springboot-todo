package com.example.springstart.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TodoNotFoundException extends RuntimeException{
    public TodoNotFoundException(Long id){
        super("해당 ID의 Todo가 없습니다: " + id);
    }
}
