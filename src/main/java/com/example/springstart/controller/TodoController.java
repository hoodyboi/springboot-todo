package com.example.springstart.controller;

import com.example.springstart.domain.Todo;
import com.example.springstart.dto.CreateTodoRequest;
import com.example.springstart.dto.UpdateTodoRequest;
import com.example.springstart.service.TodoService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @PostMapping
    public String addTodo(@RequestBody @Valid CreateTodoRequest request) {
        todoService.addTodo(request);
        return "할 일이 등록되었습니다: " + request.getTitle();
    }

    @GetMapping
    public List<Todo> getTodos() {
        return todoService.getAllTodos();
    }

    @GetMapping("/{id}")
    public Todo getTodoById(@PathVariable Long id) {
        return todoService.findById(id);
    }

    @DeleteMapping("/{id}")
    public String deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return "할 일이 삭제되었습니다: ID " + id;
    }

    @PutMapping("/{id}")
    public String updateTodo(@PathVariable Long id, @RequestBody UpdateTodoRequest request) {
        todoService.updateTodoCompleted(id, request.isCompleted());
        return "할 일 완료 상태가 변경되었습니다: ID " + id;
    }

    @GetMapping("/completed")
    public List<Todo> getCompletedTodos() {
        return todoService.getCompletedTodos();
    }

    @GetMapping("/search")
    public List<Todo> searchTodos(@RequestParam String keyword){
        return todoService.searchByTitle(keyword);
    }

    @GetMapping("/sorted")
    public List<Todo> getTodosSorted(@RequestParam String by){
        return todoService.getTodosSorted(by);
    }

    @PutMapping("/{id}/toggle")
    public String toggleTodo(@PathVariable Long id){
        todoService.toggleCompleted(id);
        return "할 일 완료 상태가 반전되었습니다: ID " + id;
    }

    @GetMapping("/page")
    public Page<Todo> getTodoByPage(@PageableDefault(size = 3) Pageable pageable){
        return todoService.getTodosByPage(pageable);
    }

    @GetMapping("/page/completed")
    public Page<Todo> getCompletedTodosPaged(Pageable pageable){
        return todoService.getCompletedTodosPaged(pageable);
    }

    @GetMapping("/page/incomplete")
    public Page<Todo> getIncompleteTodosPaged(Pageable pageable){
        return todoService.getIncompleteTodosPaged(pageable);
    }
}