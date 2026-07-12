package com.grapevine.auth.auth;

import com.grapevine.auth.user.Role;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {

    private String token;
    private String refreshToken;
    private Long id;
    private String fullName;
    private String email;
    private Role role;
    private Boolean mustChangePassword;
}