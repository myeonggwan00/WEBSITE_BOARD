package com.example.firstproject.domain.jdbc;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDateTime;

@Data
public class Post {
    private Long id;

    private Long memberId;

    @NotBlank
    private String title;

    @NotEmpty
    private String content;

    private String username;

    @DateTimeFormat(pattern = "yy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @NumberFormat(pattern = "###,###")
    private Long viewCnt = 0L;

    private Long likeCnt = 0L;

    public Post() {
    }

    public Post(String title, String content, String username) {
        this.title = title;
        this.content = content;
        this.username = username;
    }

    public void increaseViewCnt() {
        viewCnt++;
    }
}
