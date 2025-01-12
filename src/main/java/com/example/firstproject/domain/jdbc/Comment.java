package com.example.firstproject.domain.jdbc;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Long id; // cno
    private Long postId; // postBno
    private Long memberId;
    private Long parentCommentId; // pcno
    private String userId; // userId
    private String content;
    @DateTimeFormat(pattern = "yy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public Comment() {}

    public Comment(Long id, Long parentCommentId, String userId, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.parentCommentId = parentCommentId;
        this.userId = userId;
        this.content = comment;
        this.createdAt = createdAt;
    }
}
