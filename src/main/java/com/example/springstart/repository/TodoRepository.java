package com.example.springstart.repository;

import com.example.springstart.domain.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long>{
    List<Todo> findByCompletedTrue();
    List<Todo> findByTitleContaining(String keyword);
    List<Todo> findByUserUsername(String name);
    Page<Todo> findByCompletedTrue(Pageable pageable);
    Page<Todo> findByCompletedFalse(Pageable pageable);

}
