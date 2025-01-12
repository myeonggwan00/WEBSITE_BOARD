package com.example.firstproject.domain.jdbc;

import com.example.firstproject.domain.dto.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Member {
    private Long id;

    @NotBlank
    private String loginId;

    @NotBlank
    private String password;

    @NotBlank
    private String username;

    private String nickname;

    private Role role;

    private LocalDateTime createdAt;

    public Member() {}

    public Member(String loginId, String password, String username) {
        this.loginId = loginId;
        this.password = password;
        this.username = username;
    }
}
