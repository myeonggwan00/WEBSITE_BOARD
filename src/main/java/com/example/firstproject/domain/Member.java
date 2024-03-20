package com.example.firstproject.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Member {
    private Long no;

    @NotBlank
    private String id;
    @NotBlank
    private String pwd;
    @NotBlank
    private String userName;

    public Member() {}

    public Member(String id, String pwd, String userName) {
        this.id = id;
        this.pwd = pwd;
        this.userName = userName;
    }
}
