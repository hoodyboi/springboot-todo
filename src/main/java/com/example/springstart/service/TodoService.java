package com.example.springstart.service;

import com.example.springstart.domain.Todo;
import com.example.springstart.domain.User;
import com.example.springstart.dto.CreateTodoRequest;
import com.example.springstart.dto.TodoResponseDto;
import com.example.springstart.exception.TodoNotFoundException;
import com.example.springstart.repository.TodoRepository;
import com.example.springstart.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    public void addTodo(CreateTodoRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다"));
        Todo todo = new Todo(request.getTitle(), request.isCompleted(), user);
        todoRepository.save(todo);
    }

    public List<TodoResponseDto> getMyTodos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return todoRepository.findByUserUsername(username).stream()
                .map(TodoResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<TodoResponseDto> getAllTodos(){
        return todoRepository.findAll().stream()
                .map(TodoResponseDto::new)
                .collect(Collectors.toList());
    }

    public Todo findById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }

    public void updateTodoCompleted(Long id, boolean completed) {
        Todo todo = findById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (!todo.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("본인이 작성한 Todo만 수정할 수 있습니다.");
        }

        todo.setCompleted(completed);
        todoRepository.save(todo);
    }

    public void deleteTodo(Long id) {
        Todo todo = findById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        if (!todo.getUser().getUsername().equals(username)) {
            throw new IllegalArgumentException("본인이 작성한 Todo만 삭제할 수 있습니다.");
        }

        todoRepository.delete(todo);
    }


    public List<Todo> getCompletedTodos() {
        return todoRepository.findByCompletedTrue();
    }

    public List<Todo> searchByTitle(String keyword) {
        return todoRepository.findByTitleContaining(keyword);
    }

    public List<Todo> getTodosSorted(String field) {
        return todoRepository.findAll(Sort.by(Sort.Direction.ASC, field));
    }

    public void toggleCompleted(Long id) {
        Todo todo = findById(id);
        todo.setCompleted(!todo.isCompleted());
        todoRepository.save(todo);
    }

    public Page<Todo> getTodosByPage(Pageable pageable) {
        return todoRepository.findAll(pageable);
    }

    public Page<Todo> getCompletedTodosPaged(Pageable pageable) {
        return todoRepository.findByCompletedTrue(pageable);
    }

    public Page<Todo> getIncompleteTodosPaged(Pageable pageable) {
        return todoRepository.findByCompletedFalse(pageable);
    }
}
