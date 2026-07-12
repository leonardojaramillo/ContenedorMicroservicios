package com.grapevine.auth.user;

import com.grapevine.auth.user.dto.ChangePasswordRequest;
import com.grapevine.auth.user.dto.UpdateProfileRequest;
import com.grapevine.auth.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping
    public UserResponse getProfile() {
        User user = currentUser();
        return toResponse(user);
    }

    @PutMapping
    public UserResponse updateProfile(@RequestBody UpdateProfileRequest request) {
        User user = currentUser();
        if (request.getFullName() != null) user.setFullName(request.getFullName());
        if (request.getAvatar() != null) user.setAvatar(request.getAvatar());
        return toResponse(userRepository.save(user));
    }

    @PutMapping("/change-password")
    public void changePassword(@RequestBody ChangePasswordRequest request) {
        User user = currentUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Contraseña actual incorrecta");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setMustChangePassword(false);
        userRepository.save(user);
    }

    private User currentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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