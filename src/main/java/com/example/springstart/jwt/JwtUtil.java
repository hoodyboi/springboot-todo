package com.example.springstart.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    private static final String SECRET = "my-super-secret-key-that-is-long-enough-1234!";
    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
    private static final long EXPIRATION_TIME = 15 * 60 * 1000L;
    private static final long REFRESH_EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000L;

    public String createToken(String username, String role){
        return Jwts.builder()
                .setSubject(username)
                .claim("auth", role)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    public void validateTokenOrThrow(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            System.out.println(" [ACCESS] 만료된 토큰");
            throw new AuthenticationCredentialsNotFoundException("만료된 JWT 토큰입니다", e); // ✅ cause 포함!
        } catch (JwtException e) {
            System.out.println(" ACCESS] 유효하지 않은 토큰");
            throw new AuthenticationCredentialsNotFoundException("JWT 인증 실패", e); // ✅ cause 포함!
        }
    }

    public void validateRefreshTokenOrThrow(String refreshToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(refreshToken);
        } catch (ExpiredJwtException e) {
            System.out.println(" [REFRESH] 리프레시 토큰 만료됨");
            throw new AuthenticationCredentialsNotFoundException("리프레시 토큰이 만료되었습니다", e);
        } catch (JwtException e) {
            System.out.println(" [REFRESH] 리프레시 토큰 위조됨");
            throw new AuthenticationCredentialsNotFoundException("유효하지 않은 리프레시 토큰입니다", e);
        }
    }

    public String getRoleFromToken(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("auth", String.class);
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }


}