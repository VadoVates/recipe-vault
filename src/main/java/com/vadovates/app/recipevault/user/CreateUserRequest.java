package com.vadovates.app.recipevault.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserRequest(
        @NotBlank(message = "E-mail address is required")
        @Size(max = 255, message = "E-mail can't be longer than 255 characters")
        String email,

        @NotBlank(message = "Password is required")
        @Size(max = 255, message = "Password can't be longer than 255 characters")
        String password,

        @Size(max = 100, message = "Display name can't be longer than 100 characters")
        String displayName
) {
}