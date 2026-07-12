package com.grapevine.auth.auth;

import com.grapevine.auth.email.EmailService;
import com.grapevine.auth.security.JwtService;
import com.grapevine.auth.user.User;
import com.grapevine.auth.client.AuditClient;
import com.grapevine.auth.client.dto.CreateAuditLogRequest;
import com.grapevine.auth.user.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService      jwtService;
    private final EmailService    emailService;
    private final AuditClient auditClient;

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token        = jwtService.generateToken(user.getEmail(), user.getRole().name(), user.getId());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        try {
            auditClient.record(new CreateAuditLogRequest("LOGIN", "Inicio de sesión: " + user.getEmail(), user.getEmail()));
        } catch (Exception ignored) {}

        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .mustChangePassword(user.getMustChangePassword())
                .build();
    }

    public LoginResponse refresh(TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();

        if (refreshToken == null || refreshToken.isBlank()) {
            throw new RuntimeException("Refresh token requerido");
        }

        if (jwtService.isTokenExpired(refreshToken)) {
            throw new RuntimeException("Refresh token expirado");
        }

        String email = jwtService.extractEmail(refreshToken);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String newToken   = jwtService.generateToken(email, user.getRole().name(), user.getId());
        String newRefresh = jwtService.generateRefreshToken(email);

        return LoginResponse.builder()
                .token(newToken)
                .refreshToken(newRefresh)
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .mustChangePassword(user.getMustChangePassword())
                .build();
    }

    public void forgotPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email no registrado"));

        String newPassword = UUID.randomUUID().toString().substring(0, 8);
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setMustChangePassword(true);
        userRepository.save(user);

        try {
            emailService.sendPasswordReset(email, user.getFullName(), newPassword);
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
        }
    }
}