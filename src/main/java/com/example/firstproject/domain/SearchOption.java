package com.example.firstproject.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SearchOption {
    private String option;
    private String displayName;
}