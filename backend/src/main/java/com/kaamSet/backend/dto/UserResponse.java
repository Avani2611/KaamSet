package com.kaamSet.backend.dto;

import com.kaamSet.backend.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private Role role;
}