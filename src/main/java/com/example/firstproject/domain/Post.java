package com.example.firstproject.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import java.time.LocalDateTime;

@Data
public class Post {
    private Long bno;
    @NotBlank
    private String title;
    @NotEmpty
    private String content;
    private String userName;

    @DateTimeFormat(pattern = "yy-MM-dd HH:mm")
    private LocalDateTime registerTime;

    @NumberFormat(pattern = "###,###")
    private Long viewCnt = 0L;

    public Post() {
    }

    public Post(String title, String content, String userName) {
        this.title = title;
        this.content = content;
        this.userName = userName;
    }

    public void increaseViewCnt() {
        viewCnt++;
    }
}
