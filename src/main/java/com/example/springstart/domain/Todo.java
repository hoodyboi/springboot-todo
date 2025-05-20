package com.example.springstart.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Schema(description = "할 일 정보 (응답용)")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Todo {

    @Schema(description = "할 일 고유 ID", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description ="할 일 제목", example = "운동하기")
    @Column(nullable = false, length = 100)
    private String title;

    @Schema(description ="완료 여부", example = "false")
    private boolean completed;

    @Schema(description = "등록 일시", example = "2025-05-21T10:15:30")
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Schema(description ="수정 일시", example = "2025-05-21T11:00:00")
    @LastModifiedDate
    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    public Todo() {}

    public Todo(String title, boolean completed){
        this.title = title;
        this.completed = completed;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}