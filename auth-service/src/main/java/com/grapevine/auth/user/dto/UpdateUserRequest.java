package com.grapevine.auth.user.dto;

import com.grapevine.auth.user.Role;

import lombok.Data;

@Data
public class UpdateUserRequest {

    private String fullName;
    private Role role;
    private Boolean enabled;
}