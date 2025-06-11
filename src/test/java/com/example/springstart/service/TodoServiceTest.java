package com.example.springstart.service;

import com.example.springstart.domain.Todo;
import com.example.springstart.domain.User;
import com.example.springstart.dto.CreateTodoRequest;
import com.example.springstart.dto.TodoResponseDto;
import com.example.springstart.exception.TodoNotFoundException;
import com.example.springstart.repository.TodoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @InjectMocks
    private TodoService todoService;

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private UserService userService;

    @Test
    void 할일을_등록하면_저장된다() {
        // given
        User user = new User("jm4", "encodedpw", "ROLE_USER");
        CreateTodoRequest request = new CreateTodoRequest("공부하기", false);
        given(userService.getCurrentUser()).willReturn(user);

        // when
        todoService.addTodo(request);

        // then
        then(todoRepository).should().save(any(Todo.class));
    }

    @Test
    void toggleCompleted_should_work() {
        // given
        User user = new User("jm4", "pw", "ROLE_USER");
        Todo todo = new Todo("청소하기", false, user);
        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        // when
        todoService.toggleCompleted(1L);

        // then
        assertTrue(todo.isCompleted());
        verify(todoRepository).save(any(Todo.class));
    }

    @Test
    void deleteTodo_shouldThrowException_whenUserIsNotOwner() {
        // given
        User user = new User("jm4", "pw", "ROLE_USER");
        User user1 = new User("jm3", "pw", "ROLE_USER");
        Todo todo = new Todo("운동하기", false, user1);

        given(userService.getCurrentUser()).willReturn(user);
        given(todoRepository.findById(1L)).willReturn(Optional.of(todo));

        assertThrows(IllegalArgumentException.class, () ->{
            todoService.deleteTodo(1L);
        });
    }

    @Test
    void deleteTodo_shouldThrowException_whenTodoNotFound(){
        //given
        given(todoRepository.findById(999L)).willReturn(Optional.empty());

        //when & then
        assertThrows(TodoNotFoundException.class, () -> {
            todoService.deleteTodo(999L);
        });
    }

    @Test
    void updateTodoCompleted_shouldUpdateCompletedStatus(){
        //given
        User user = new User("jm4", "pw", "ROLE_USER");
        Todo todo = new Todo("자바 공부", false, user);

        given(userService.getCurrentUser()).willReturn(user);
        given(todoRepository.findById(1L)).willReturn(Optional.of(todo));

        //when
        todoService.updateTodoCompleted(1L, true);

        //then
        assertTrue(todo.isCompleted());
        verify(todoRepository).save(todo);
    }

    @Test
    void getMyTodos_shouldReturnUserTodos(){
        //given
        User user = new User("jm4", "pw", "ROLE_USER");
        Todo todo1 = new Todo("할 일1", false, user);
        Todo todo2 = new Todo("할 일2", true, user);

        given(userService.getCurrentUser()).willReturn(user);
        given(todoRepository.findByUserUsername("jm4")).willReturn(List.of(todo1, todo2));

        //when
        List<TodoResponseDto> result = todoService.getMyTodos();

        //then
        assertEquals(2, result.size());
        assertEquals("할 일1", result.get(0).getTitle());
        assertEquals("할 일2", result.get(1).getTitle());
    }

    @Test
    void searchByTitle_shouldReturnMatchedTodos(){
        //given
        User user = new User("jm4", "pw", "ROLE_USER");
        Todo t1 = new Todo("자바 공부", false, user);
        Todo t2 = new Todo("운동하기", false, user);
        Todo t3 = new Todo("알고리즘 공부", true, user);

        given(todoRepository.findByTitleContaining("공부"))
                .willReturn(List.of(t1, t3));

        //when
        List<Todo> result = todoService.searchByTitle("공부");

        //then
        assertEquals(2, result.size());
        assertEquals("자바 공부", result.get(0).getTitle());
        assertEquals("알고리즘 공부", result.get(1).getTitle());
    }

    @Test
    void getTodosSorted_shouldReturnSortedTodos(){
        //given

        User user = new User("jm4", "pw", "ROLE_USER");

        Todo todo1 = new Todo("공부하기", false, user);
        Todo todo2 = new Todo("밥먹기", false, user);
        Todo todo3 = new Todo("청소하기", false, user);

        List<Todo> sortedTodos = List.of(todo1, todo2, todo3);

        given(todoRepository.findAll(Sort.by(Sort.Direction.ASC, "title")))
                .willReturn(sortedTodos);

        //when
        List<Todo> result = todoService.getTodosSorted("title");

        //then
        assertEquals("공부하기", result.get(0).getTitle());
        assertEquals("밥먹기", result.get(1).getTitle());
        assertEquals("청소하기", result.get(2).getTitle());
    }

    @Test
    void getTodosSortedByTitleDesc_shouldReturnSortedList(){
        //given
        User user = new User("jm4", "pw", "ROLE_USER");
        Todo todo1 = new Todo("청소하기", false, user);
        Todo todo2 = new Todo("운동하기", false, user);
        Todo todo3 = new Todo("자바 공부", false, user);

        List<Todo> todos = List.of(todo1,todo2,todo3).stream()
                        .sorted((a,b) -> b.getTitle().compareTo(a.getTitle()))
                                .collect(Collectors.toList());

        given(userService.getCurrentUser()).willReturn(user);
        given(todoRepository.findByUserUsername("jm4")).willReturn(todos);

        //when
        List<TodoResponseDto> result = todoService.getTodosSortedByTitleDesc();

        //then
        assertEquals("청소하기", result.get(0).getTitle());
        assertEquals("자바 공부", result.get(1).getTitle());
        assertEquals("운동하기", result.get(2).getTitle());
    }

    @Test
    void getTodosSorted_shouldReturnTodosInAscOrder(){
        //given
        User user = new User("jm4", "pw", "ROLE_USER");
        Todo t1 = new Todo("청소하기", false, user);
        Todo t2 = new Todo("밥하기", false, user);
        Todo t3 = new Todo("공부하기", false, user);

        List<Todo> todos = List.of(t1, t2, t3);

        given(userService.getCurrentUser()).willReturn(user);
        given(todoRepository.findByUserUsername("jm4"))
                .willReturn(todos);

        //when
        List<TodoResponseDto> result = todoService.getTodosSortedByTitleAsc();

        //then
        assertEquals("공부하기", result.get(0).getTitle());
        assertEquals("밥하기", result.get(1).getTitle());
        assertEquals("청소하기", result.get(2).getTitle());
    }

    @Test
    void getCompletedTodosPaged_shouldReturnCompleted(){
        //given
        User user = new User("jm4", "pw", "ROLE_USER");
        Todo t1 = new Todo("운동하기", true, user);
        Todo t2 = new Todo("자바 공부", true, user);
        Page<Todo> page = new PageImpl<>(List.of(t1,t2));

        given(todoRepository.findByCompletedTrue(any(Pageable.class)))
                .willReturn(page);

        //when
        Page<Todo> result = todoService.getCompletedTodosPaged(PageRequest.of(0,10));

        //then
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().get(0).isCompleted());
    }

    @Test
    void getCompletedTodosPaged_shouldReturnSortedPage(){
        //given
        User user = new User("jm4", "pw","ROLE_USER");
        Todo t1 = new Todo("공부하기", true, user);
        Todo t2 = new Todo("운동하기", true, user);
        Todo t3 = new Todo("청소하기", true, user);

        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "title"));

        Page<Todo> page = new PageImpl<>(
                List.of(t1, t2, t3).stream()
                        .sorted((a,b) -> a.getTitle().compareTo(b.getTitle()))
                        .collect(Collectors.toList()),
                pageable,
                3
        );

        given(todoRepository.findByCompletedTrue(pageable))
                .willReturn(page);

        //when
        Page<Todo> result = todoService.getCompletedTodosPaged(pageable);

        //then
        assertEquals(3, result.getContent().size());
        assertEquals("공부하기", result.getContent().get(0).getTitle());
        assertEquals("운동하기", result.getContent().get(1).getTitle());
        assertEquals("청소하기", result.getContent().get(2).getTitle());
    }

    @Test
    void getCompletedTodosByUserpaged_shouldReturnFilteredAndSortedPage(){
        //given
        User user = new User("jm4", "pw", "ROLE_USER");
        Todo t1 = new Todo("공부하기", true, user);
        Todo t2 = new Todo("운동하기", true, user);
        Todo t3 = new Todo("청소하기", true, user);

        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "title"));

        Page<Todo> page = new PageImpl<>(
                List.of(t1, t2, t3).stream()
                        .sorted((a,b) -> a.getTitle().compareTo(b.getTitle()))
                        .collect(Collectors.toList()),
                pageable,
                3
        );

        given(userService.getCurrentUser()).willReturn(user);
        given(todoRepository.findByUserUsernameAndCompletedTrue("jm4", pageable))
                .willReturn(page);

        //when
        Page<Todo> result = todoService.getCompletedTodosByUserPaged(pageable);

        //then
        assertEquals(3, result.getContent().size());
        assertEquals("공부하기", result.getContent().get(0).getTitle());
        assertEquals("운동하기", result.getContent().get(1).getTitle());
        assertEquals("청소하기", result.getContent().get(2).getTitle());
    }

}