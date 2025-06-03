package com.example.springstart.controller;

import com.example.springstart.dto.LoginRequestDto;
import com.example.springstart.dto.UserRequestDto;
import com.example.springstart.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    @Operation(
            summary     = "로그인",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content  = @Content(
                            mediaType = "application/json",
                            examples  = @ExampleObject(
                                    name        = "성공 예시",
                                    description = "존재하는 ID / PW",
                                    value =
                                            """
                                            {
                                              "username": "jm4",
                                              "password": "jm1234"
                                            }
                                            """
                            )
                    )
            )
    )
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDto dto) {
        Map<String, String> tokens = userService.login(dto);
        return ResponseEntity.ok(tokens);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/admin-only")
    public String adminOnly(){
        return "관리자만 접근 가능한 API입니다.";
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<?> logout(HttpServletRequest request){

        String refreshToken = request.getHeader("Authorization");
        if(refreshToken != null && refreshToken.startsWith("Bearer ")){
            refreshToken = refreshToken.substring(7);
        }
        userService.logout(refreshToken);
        return ResponseEntity.ok().body("로그아웃 되었습니다.");
    }

    @PostMapping("/reissue")
    @Operation(summary = "리프레시 토큰으로 액세스 토큰 재발급")
    public ResponseEntity<?> reissue(HttpServletRequest request){

        String refreshToken = request.getHeader("Authorization");
        if(refreshToken != null && refreshToken.startsWith("Bearer ")){
            refreshToken = refreshToken.substring(7);
        }
        Map<String, String> token = userService.reissueToken(refreshToken);
        return ResponseEntity.ok(token);
    }
}
