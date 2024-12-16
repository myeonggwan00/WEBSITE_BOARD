package com.example.firstproject.domain;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class Comment {
    private Long cno;
    private Long pcno;
    private Long postBno;
    private String userId;
    private String content;
    @DateTimeFormat(pattern = "yy-MM-dd HH:mm:ss")
    private LocalDateTime registerTime;

    public Comment() {}

    public Comment(Long pcno, Long postBno, String userId, String comment, LocalDateTime registerTime) {
        this.pcno = pcno;
        this.postBno = postBno;
        this.userId = userId;
        this.content = comment;
        this.registerTime = registerTime;
    }
}
