package com.example.springstart.controller;

import com.example.springstart.dto.TodoResponseDto;
import com.example.springstart.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TestController {

    private final TodoRepository todoRepository;

    /** 1️⃣ N+1 재현 */
    @GetMapping("/api/test/nplus")
    public List<TodoResponseDto> testNPlus() {
        return todoRepository.findAll()                      // 순수 LAZY
                .stream()
                .map(TodoResponseDto::from)
                .toList();
    }

    /** 2️⃣ Fetch-Join (@EntityGraph) 테스트 */
    @GetMapping("/api/test/graph")
    public List<TodoResponseDto> testEntityGraph() {
        return todoRepository.findAllWithGraph()             // Fetch-Join
                .stream()
                .map(TodoResponseDto::from)
                .toList();
    }

    /** 3️⃣ BatchSize 테스트 */
    @GetMapping("/api/test/batch")
    public List<TodoResponseDto> testBatch() {
        return todoRepository.findAll()                      // 순수 LAZY + @BatchSize
                .stream()
                .map(TodoResponseDto::from)
                .toList();
    }

}