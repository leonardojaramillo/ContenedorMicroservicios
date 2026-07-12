package com.grapevine.auth.user.dto;

import com.grapevine.auth.user.Role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    private Long id;
    private String fullName;
    private String email;
    private Role role;
    private Boolean enabled;
    private Boolean mustChangePassword;
}