package com.example.springstart.dto;

import lombok.Getter;

@Getter
public class UserRequestDto {
    private String username;
    private String password;
    private String role;

    public String getRole(){
        return role;
    }
}
