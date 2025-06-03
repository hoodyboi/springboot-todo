package com.example.springstart.service;

import com.example.springstart.domain.RefreshToken;
import com.example.springstart.domain.User;
import com.example.springstart.dto.LoginRequestDto;
import com.example.springstart.dto.UserRequestDto;
import com.example.springstart.jwt.JwtUtil;
import com.example.springstart.repository.RefreshTokenRepository;
import com.example.springstart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    public void signup(UserRequestDto requestDto){
        if (userRepository.findByUsername(requestDto.getUsername()).isPresent()){
            throw new IllegalArgumentException("중복된 사용자 입니다.");
        }
        String role = requestDto.getRole();
        if(role == null || (!role.equals("ROLE_USER") && !role.equals("ROLE_ADMIN"))){
            role = "ROLE_USER";
        }
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(requestDto.getUsername(), encodedPassword, role);
        userRepository.save(user);
    }

    public Map<String, String> login(LoginRequestDto requestDto){
        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 사용자입니다"));

        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        String accessToken = jwtUtil.createToken(user.getUsername(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getUsername());

        refreshTokenRepository.findByUsername(user.getUsername())
                .ifPresentOrElse(
                        token -> refreshTokenRepository.save(token.updateToken(refreshToken)),
                        () -> refreshTokenRepository.save(new RefreshToken(user.getUsername(), refreshToken))
                );

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    public User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    public void logout(String refreshToken){
        refreshTokenRepository.findByToken(refreshToken.trim())
            .ifPresent(refreshTokenRepository::delete);
    }

    public Map<String, String> reissueToken(String refreshToken){
        refreshToken = refreshToken.trim();
        jwtUtil.validateRefreshTokenOrThrow(refreshToken);

        String username = jwtUtil.getUsernameFromToken(refreshToken);

        RefreshToken saved = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("리프레시 토큰이 존재하지 않습니다."));

        if (!saved.getToken().equals(refreshToken)){
            throw new IllegalArgumentException("리프레시 토큰이 일치하지 않습니다.");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        String newAccess = jwtUtil.createToken(username, user.getRole());
        return Map.of("accessToken", newAccess);
    }


}