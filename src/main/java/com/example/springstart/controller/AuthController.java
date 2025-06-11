package com.example.springstart.controller;

import com.example.springstart.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;          // ✔️
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @PostMapping("/logout")
    @Operation(summary = "로그아웃")
    public ResponseEntity<String> logout(HttpServletRequest request) {

        String token = jwtUtil.resolveToken(request);

        if (token != null && jwtUtil.validateToken(token)) {
            long ttl = jwtUtil.getExpiration(token);
            redisTemplate.opsForValue()
                    .set(token, "logout", ttl, TimeUnit.MILLISECONDS);
        }
        return ResponseEntity.ok("로그아웃 완료");
    }
}