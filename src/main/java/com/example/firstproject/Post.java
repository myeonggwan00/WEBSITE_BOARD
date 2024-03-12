package com.example.firstproject;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class Post {
    private Long bno;
    @NotBlank
    private String title;
    @NotEmpty
    private String content;
    private String userName;
    private String registerTime;

    private Long viewCnt = 0L;

    public Post() {
    }

    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void increaseViewCnt() {
        viewCnt++;
    }
}
