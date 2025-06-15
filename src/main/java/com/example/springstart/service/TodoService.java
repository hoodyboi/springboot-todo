package com.example.springstart.service;

import com.example.springstart.domain.Todo;
import com.example.springstart.domain.User;
import com.example.springstart.dto.CreateTodoRequest;
import com.example.springstart.dto.TodoResponseDto;
import com.example.springstart.exception.TodoNotFoundException;
import com.example.springstart.repository.TodoRepository;
import com.example.springstart.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository, UserService userService) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    // 중복 제거용 메서드: 인증 사용자와 Todo 작성자가 같은지 확인
    private void checkOwnership(Todo todo, User user){
        if (!todo.getUser().getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("본인이 작성한 Todo만 수정/삭제할 수 있습니다.");
        }
    }

    // Todo 등록
    public void addTodo(CreateTodoRequest request) {
        User user = userService.getCurrentUser();
        Todo todo = new Todo(request.getTitle(), request.isCompleted(), user);
        todoRepository.save(todo);
    }

    // 로그인 사용자의 Todo 전체 조회
    public List<TodoResponseDto> getMyTodos() {
        User user = userService.getCurrentUser();

        return todoRepository.findByUserUsername(user.getUsername()).stream()
                .map(TodoResponseDto::new)
                .collect(Collectors.toList());
    }

    // 전체 사용자 Todo 조회 (관리자용)
    public List<TodoResponseDto> getAllTodos() {
        return todoRepository.findAll().stream()
                .map(TodoResponseDto::new)
                .collect(Collectors.toList());
    }

    // ID로 Todo 조회 (내부용)
    public Todo findById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }

    // Todo 완료 여부 수정 (소유자만 가능)
    public void updateTodoCompleted(Long id, boolean completed) {
        User user = userService.getCurrentUser();
        Todo todo = findById(id);

        checkOwnership(todo, user);

        todo.setCompleted(completed);
        todoRepository.save(todo);
    }

    // Todo 단일 삭제 (소유자만 가능)
    public void deleteTodo(Long id) {
        User user = userService.getCurrentUser();
        Todo todo = findById(id);

        checkOwnership(todo, user);

        todoRepository.delete(todo);
    }

    // 모든 Todo 삭제 (관리자용)
    public void deleteAllTodos() {
        todoRepository.deleteAll();
    }

    // 완료된 Todo 조회
    public List<Todo> getCompletedTodos() {
        return todoRepository.findByCompletedTrue();
    }

    // 제목으로 Todo 검색
    public List<Todo> searchByTitle(String keyword) {
        return todoRepository.findByTitleContaining(keyword);
    }

    // 특정 필드 기준 정렬
    public List<Todo> getTodosSorted(String field) {
        return todoRepository.findAll(Sort.by(Sort.Direction.ASC, field));
    }

    // 완료 여부 토글
    public void toggleCompleted(Long id) {
        Todo todo = findById(id);
        todo.setCompleted(!todo.isCompleted());
        todoRepository.save(todo);
    }

    // 전체 Todo 페이징
    public Page<Todo> getTodosByPage(Pageable pageable) {
        return todoRepository.findAll(pageable);
    }

    // 완료된 Todo 페이징
    public Page<Todo> getCompletedTodosPaged(Pageable pageable) {
        return todoRepository.findByCompletedTrue(pageable);
    }

    // 미완료 Todo 페이징
    public Page<Todo> getIncompleteTodosPaged(Pageable pageable) {
        return todoRepository.findByCompletedFalse(pageable);
    }

    public List<TodoResponseDto> getTodosSortedByTitleDesc(){
        User user = userService.getCurrentUser();
        return todoRepository.findByUserUsername(user.getUsername()).stream()
                .sorted((a,b) -> b.getTitle().compareTo(a.getTitle()))
                .map(TodoResponseDto::new)
                .collect((Collectors.toList()));
    }

    public List<TodoResponseDto> getTodosSortedByTitleAsc(){
        User user = userService.getCurrentUser();

        return todoRepository.findByUserUsername(user.getUsername()).stream()
                .sorted((a,b) -> Collator.getInstance().compare(a.getTitle(), b.getTitle()))
                .map(TodoResponseDto::new)
                .collect(Collectors.toList());
    }

    public Page<TodoResponseDto> getTodoDtosByPage(Pageable pageable){
        Page<Todo> todos = todoRepository.findAll(pageable);
        return todos.map(TodoResponseDto::new);
    }

    public Page<Todo> getCompletedTodosByUserPaged(Pageable pageable){
        String username = userService.getCurrentUser().getUsername();
        return todoRepository.findByUserUsernameAndCompletedTrue(username, pageable);
    }

    public Page<Long> findTodoIds(Pageable pageable){
        return todoRepository.findTodoIds(pageable);
    }

    public List<Todo> findTodoWithUserByIds(List<Long> ids){
        return todoRepository.findWithUserByIdIn(ids);
    }
}