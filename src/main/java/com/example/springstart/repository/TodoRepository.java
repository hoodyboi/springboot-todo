package com.example.springstart.repository;

import com.example.springstart.domain.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    // ──────────────────────────────────────────────
    // 1) Fetch-Join(EntityGraph) 전용 메서드
    // ──────────────────────────────────────────────
    @EntityGraph(attributePaths = "user")
    @Query("select t from Todo t")
    List<Todo> findAllWithGraph();

    @EntityGraph(attributePaths = "user")
    List<Todo> findByUserUsername(String username);
    // ──────────────────────────────────────────────


    // ──────────────────────────────────────────────
    // 2) 순수 LAZY 조회용 (BatchSize 테스트용)
    // ──────────────────────────────────────────────
    @Override
    List<Todo> findAll();

    // (기존에 이 메서드에도 @EntityGraph가 붙어 있었으면 제거하세요)
    // ──────────────────────────────────────────────


    // ──────────────────────────────────────────────
    // 3) 기존 메서드들 (필터·페이징·검색)
    // ──────────────────────────────────────────────
    List<Todo> findByCompletedTrue();
    List<Todo> findByTitleContaining(String keyword);

    Page<Todo> findByCompletedTrue(Pageable pageable);
    Page<Todo> findByCompletedFalse(Pageable pageable);

    @EntityGraph(attributePaths = "user")
    Page<Todo> findByUserUsernameAndCompletedTrue(String username, Pageable pageable);
    // ──────────────────────────────────────────────


    // ──────────────────────────────────────────────
    // 4) 두 쿼리 전략 (페이징 최적화용)
    // ──────────────────────────────────────────────
    @Query("select t.id from Todo t")
    Page<Long> findTodoIds(Pageable pageable);

    @Query("""
        select distinct t
        from Todo t
        join fetch t.user
        where t.id in :ids
    """)
    List<Todo> findWithUserByIdIn(@Param("ids") List<Long> ids);
    // ──────────────────────────────────────────────
}