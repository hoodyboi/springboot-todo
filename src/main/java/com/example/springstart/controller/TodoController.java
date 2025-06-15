package com.example.springstart.controller;

import com.example.springstart.domain.Todo;
import com.example.springstart.dto.CreateTodoRequest;
import com.example.springstart.dto.LoginRequestDto;
import com.example.springstart.dto.TodoResponseDto;
import com.example.springstart.dto.UpdateTodoRequest;
import com.example.springstart.service.TodoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @Operation(summary = "할 일 등록", description = "새로운 할 일(Todo)을 등록합니다")
    @ApiResponses(value ={
            @ApiResponse(responseCode ="201", description = "할 일 등록 성공"),
            @ApiResponse(responseCode = "400", description = "입력값 검증 실패")
    })
    @PostMapping
    public String addTodo(@RequestBody @Valid CreateTodoRequest request) {
        todoService.addTodo(request);
        return "할 일이 등록되었습니다: " + request.getTitle();
    }

    @Operation(
            summary = "모든 할 일 페이지네이션 조회",
            description = "Todo + User를 fetch-join으로 가져옵니다. page, size, sort 파라미터 사용 가능"
    )
    @GetMapping
    public Page<TodoResponseDto> getTodos(Pageable pageable){
        Page<Long> idPage = todoService.findTodoIds(pageable);
        List<TodoResponseDto> content = todoService.findTodoWithUserByIds(idPage.getContent())
                .stream()
                .map(TodoResponseDto::from)
                .toList();
        return new PageImpl<>(content, pageable, idPage.getTotalElements());
    }

    @Operation(
            summary = "할 일 단건 조회",
            description = "ID로 특정 할 일을 조회합니다."
    )
    @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "성공적으로 조회함"),
                @ApiResponse(responseCode = "404", description = "해당 ID가 존재하지 않음")
                })
    @GetMapping("/{id:[0-9]+}")
    public Todo getTodoById(@PathVariable Long id) {
        return todoService.findById(id);
    }

    @Operation(summary = "할 일 단건 삭제", description = "입력된 ID로 등록 된 할 일을 삭제합니다")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 ID")
    })
    @DeleteMapping("/{id}")
    public String deleteTodo(@PathVariable Long id) {
        todoService.deleteTodo(id);
        return "할 일이 삭제되었습니다: ID " + id;
    }

    @Operation(summary = "할 일 단건 상태변경", description = "입력된 ID로 등록된 할 일의 상태를 변경합니다")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "상태 변경 성공"),
            @ApiResponse(responseCode = "404", description = "해당 ID가 존재하지 않음")
    })
    @PutMapping("/{id}")
    public String updateTodo(@PathVariable Long id, @RequestBody UpdateTodoRequest request) {
        todoService.updateTodoCompleted(id, request.isCompleted());
        return "할 일 완료 상태가 변경되었습니다: ID " + id;
    }

    @Operation(summary = "완료된 할 일 조회", description = "완료된 모든 할 일을 조회합니다")
    @GetMapping("/completed")
    public List<Todo> getCompletedTodos() {
        return todoService.getCompletedTodos();
    }

    @Operation(summary = "키워드 조회", description = "입력된 키워드를 조회합니다")
    @GetMapping("/search")
    public List<Todo> searchTodos(@RequestParam String keyword){
        return todoService.searchByTitle(keyword);
    }

    @Operation(summary = "정렬", description = "입력된 키워드에 따라 조회됩니다")
    @GetMapping("/sorted")
    public List<Todo> getTodosSorted(@RequestParam String by){
        return todoService.getTodosSorted(by);
    }

    @Operation(summary = "상태 반전", description = "등록된 할 일의 상태를 반전합니다")
    @PutMapping("/{id}/toggle")
    public String toggleTodo(@PathVariable Long id){
        todoService.toggleCompleted(id);
        return "할 일 완료 상태가 반전되었습니다: ID " + id;
    }

    @Operation(
            summary = "할 일 목록 페이징 조회",
            description = "등록된 모든 할 일을 페이지 단위로 조회합니다. page, size, sort 파라미터를 사용할 수 있습니다."
    )
    @GetMapping("/page")
    public Page<Todo> getTodoByPage(@PageableDefault(size = 3) Pageable pageable){
        return todoService.getTodosByPage(pageable);
    }

    @Operation(
            summary = "완료된 할 일 페이징 조회",
            description = "완료된 할 일을 페이지 단위로 조회합니다. page, size, sort 파라미터를 사용할 수 있습니다."
    )
    @GetMapping("/page/completed")
    public Page<Todo> getCompletedTodosPaged(Pageable pageable){
        return todoService.getCompletedTodosPaged(pageable);
    }

    @Operation(
            summary = "미완료된 할 일 페이징 조회",
            description = "완료되지 않은 할 일을 페이지 단위로 조회합니다. page, size, sort 파라미터를 사용할 수 있습니다."
    )
    @GetMapping("/page/incomplete")
    public Page<Todo> getIncompleteTodosPaged(Pageable pageable){
        return todoService.getIncompleteTodosPaged(pageable);
    }

    @Operation(
        summary = "내 할 일 조회",
        description = "로그인한 사용자의 할 일만 조회합니다. JWT 인증이 필요합니다.",
        security = { @SecurityRequirement(name = "JWT")}
    )
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "할 일 목록 조회 성공"),
            @ApiResponse(responseCode = "401", description = "JWT 인증 실패 또는 만료")
    })
    @GetMapping("/my")
    public ResponseEntity<List<TodoResponseDto>> getMyTodos(){
        List<TodoResponseDto> myTodos = todoService.getMyTodos();
        return ResponseEntity.ok(myTodos);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/admin/todos")
    public ResponseEntity<?> deleteAllTodos(){
        todoService.deleteAllTodos();
        return ResponseEntity.ok().body("모든 할 일이 삭제되었습니다.");
    }
}