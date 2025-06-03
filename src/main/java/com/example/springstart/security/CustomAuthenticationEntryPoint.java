package com.example.springstart.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {

        String exception = (String) request.getAttribute("exception");
        String message = "";
        String code = "";

        if ("EXPIRED_TOKEN".equals(exception)) {
            message = "토큰이 만료되었습니다";
            code = "EXPIRED_TOKEN";
            System.out.println("🔥 EntryPoint - 만료된 토큰 감지됨");
        } else if ("INVALID_TOKEN".equals(exception)) {
            message = "유효하지 않은 토큰입니다";
            code = "INVALID_TOKEN";
            System.out.println("⚠️ EntryPoint - 유효하지 않은 토큰 감지됨");
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        response.getWriter().write(
                String.format("{\"message\": \"%s\", \"code\": \"%s\"}", message, code)
        );
    }
}