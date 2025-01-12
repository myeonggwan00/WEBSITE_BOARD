package com.example.firstproject.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginMember {
    @NotBlank
    private String id;
    @NotBlank
    private String pwd;

    private boolean rememberId;

    public LoginMember() {}

    public LoginMember(String id) {
        this.id = id;
    }
}
