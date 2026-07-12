package com.grapevine.auth.auth;

import lombok.Data;

@Data
public class TokenRefreshRequest {
    private String refreshToken;
}