package com.example.springstart.controller;

import com.example.springstart.dto.LoginRequestDto;
import com.example.springstart.dto.UserRequestDto;
import com.example.springstart.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "회원 가입"
    )
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserRequestDto requestDto){
        userService.signup(requestDto);
        return ResponseEntity.ok("회원가입 성공");
    }

    @Operation(
            summary = "로그인"
    )
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDto requestDto){
        String token = userService.login(requestDto);

        Map<String, String> response = new HashMap<>();
        response.put("message", "로그인 성공");
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
}
