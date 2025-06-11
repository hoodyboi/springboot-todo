package com.example.springstart.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    private static final long ACCESS_EXP = 15 * 60 * 1000L;
    private static final long REFRESH_EXP = 7 * 24 * 60 * 60 * 1000L;

    private Key getKey(){
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createToken(String username, String role){
        return Jwts.builder()
                .setSubject(username)
                .claim("auth", role)
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXP))
                .signWith(getKey(),SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String username){
        return Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXP))
                .signWith(getKey())
                .compact();
    }

    public String resolveToken(HttpServletRequest request){
        String bearer = request.getHeader("Authorization");
        return (bearer != null && bearer.startsWith("Bearer "))
                ? bearer.substring(7)
                : null;
    }

    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e){
            return false;
        }
    }

    public long getExpiration(String token){
        Claims c = Jwts.parserBuilder().setSigningKey(getKey()).build()
                .parseClaimsJws(token).getBody();
        return c.getExpiration().getTime() - System.currentTimeMillis();
    }

    public void validateTokenOrThrow(String token){
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new AuthenticationCredentialsNotFoundException("만료된 JWT 토큰입니다", e);
        } catch (JwtException e) {
            throw new AuthenticationCredentialsNotFoundException("JWT 인증 실패");
        }
    }

    public void validateRefreshTokenOrThrow(String refreshToken){
        try {
            Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(refreshToken);
        } catch (ExpiredJwtException e){
            throw new AuthenticationCredentialsNotFoundException("리프레시 토큰이 만료되었습니다", e);
        } catch (JwtException e){
            throw new AuthenticationCredentialsNotFoundException("유효하지 않은 리프레시 토큰입니다", e);
        }
    }

    public String getRoleFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(getKey()).build()
                .parseClaimsJws(token).getBody()
                .get("auth", String.class);
    }

    public String getUsernameFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(getKey()).build()
                .parseClaimsJws(token).getBody()
                .getSubject();
    }

}