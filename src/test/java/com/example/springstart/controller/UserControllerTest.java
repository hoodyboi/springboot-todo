package com.example.springstart.controller;

import com.example.springstart.dto.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;

    @Test
    void login_success_returns_tokens() throws Exception {
        LoginRequestDto dto = new LoginRequestDto("jm4", "1234");

        mvc.perform(post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.refreshToken").exists());
    }

    @Test
    void todo_requires_authentication() throws Exception {
        mvc.perform(get("/api/todos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "jm4", roles = "USER")
    void admin_api_forbidden_for_user() throws Exception {
        mvc.perform(get("/api/admin-only"))
                .andExpect(status().isForbidden());
    }
}