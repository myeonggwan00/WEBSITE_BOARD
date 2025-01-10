package com.example.firstproject.domain;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Member {
    private Long id;

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;

    @NotBlank
    private String username;

    public Member() {}

    public Member(String loginId, String password, String username) {
        this.loginId = loginId;
        this.password = password;
        this.username = username;
    }
}
