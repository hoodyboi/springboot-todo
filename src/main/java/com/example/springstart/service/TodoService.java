package com.example.springstart.service;

import com.example.springstart.domain.Todo;
import com.example.springstart.dto.CreateTodoRequest;
import com.example.springstart.exception.TodoNotFoundException;
import com.example.springstart.repository.TodoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository){
        this.todoRepository =todoRepository;
    }

    public void addTodo(CreateTodoRequest request){
        Todo todo = new Todo(request.getTitle(), request.isCompleted());
        todoRepository.save(todo);
    }

    public List<Todo> getAllTodos(){
        return todoRepository.findAll();
    }

    public Todo findById(Long id){
        return todoRepository.findById(id)
                .orElseThrow(() -> new TodoNotFoundException(id));
    }

    public void deleteTodo(Long id){
        if(!todoRepository.existsById(id)){
            throw new TodoNotFoundException(id);
        }
        todoRepository.deleteById(id);
    }

    public void updateTodoCompleted(Long id, boolean completed){
        Todo todo = findById(id);
        todo.setCompleted(completed);
        todoRepository.save(todo);
    }

    public List<Todo> getCompletedTodos(){
        return todoRepository.findByCompletedTrue();
    }

    public List<Todo> searchByTitle(String keyword){
        return todoRepository.findByTitleContaining(keyword);
    }

    public List<Todo> getTodosSorted(String field){
        return todoRepository.findAll(Sort.by(Sort.Direction.ASC, field));
    }

    public void toggleCompleted(Long id){
        Todo todo = findById(id);
        todo.setCompleted(!todo.isCompleted());
        todoRepository.save(todo);
    }

    public Page<Todo> getTodosByPage(Pageable pageable){
        return todoRepository.findAll(pageable);
    }

    public Page<Todo> getCompletedTodosPaged(Pageable pageable){
        return todoRepository.findByCompletedTrue(pageable);
    }

    public Page<Todo> getIncompleteTodosPaged(Pageable pageable){
        return todoRepository.findByCompletedFalse(pageable);
    }
}
