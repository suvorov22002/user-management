package com.pyramid.usermanagement.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record UserDTO(
        Long Id,
        @NotBlank(message = "Login is required")
        String login,
        @NotBlank(message = "Name is required")
        String name,
        @NotBlank(message = "Email is required")
        @Email(message = "Email should be valid")
        String email,
        LocalDateTime createdDate
) {
}
