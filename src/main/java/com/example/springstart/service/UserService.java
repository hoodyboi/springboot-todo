package com.example.springstart.service;

import com.example.springstart.domain.User;
import com.example.springstart.dto.LoginRequestDto;
import com.example.springstart.dto.UserRequestDto;

import com.example.springstart.jwt.JwtUtil;
import com.example.springstart.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public void signup(UserRequestDto requestDto){
        if (userRepository.findByUsername(requestDto.getUsername()).isPresent()){
            throw new IllegalArgumentException("중복된 사용자 입니다.");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = new User(requestDto.getUsername(), encodedPassword);
        userRepository.save(user);
    }

    public String login(LoginRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 사용자입니다"));

        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        return jwtUtil.createToken(user.getUsername());
    }
}
