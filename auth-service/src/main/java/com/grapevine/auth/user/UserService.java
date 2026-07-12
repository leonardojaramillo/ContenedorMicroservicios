package com.grapevine.auth.user;

import com.grapevine.auth.email.EmailService;
import com.grapevine.auth.user.dto.CreateUserRequest;
import com.grapevine.auth.user.dto.UpdateUserRequest;
import com.grapevine.auth.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository    userRepository;
    private final PasswordEncoder   passwordEncoder;
    private final EmailService      emailService;

    public UserResponse create(CreateUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        String generatedPassword = UUID.randomUUID().toString().substring(0, 8);

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(generatedPassword))
                .role(request.getRole())
                .enabled(true)
                .mustChangePassword(true)
                .build();

        User saved = userRepository.save(user);


        try {
            emailService.sendUserCredentials(saved.getEmail(), saved.getFullName(), generatedPassword);
        } catch (Exception e) {
            System.out.println("Error sending email: " + e.getMessage());
        }

        return toResponse(saved);
    }

    public List<UserResponse> findAll() {
        return userRepository.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponse update(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getRole() != null) user.setRole(request.getRole());
        if (request.getEnabled() != null) user.setEnabled(request.getEnabled());

        User saved = userRepository.save(user);


        return toResponse(saved);
    }

    private UserResponse toResponse(User u) {
        return UserResponse.builder()
                .id(u.getId())
                .fullName(u.getFullName())
                .email(u.getEmail())
                .role(u.getRole())
                .enabled(u.getEnabled())
                .mustChangePassword(u.getMustChangePassword())
                .build();
    }
}