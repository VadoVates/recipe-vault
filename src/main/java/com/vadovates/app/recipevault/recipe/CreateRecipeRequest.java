package com.vadovates.app.recipevault.recipe;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateRecipeRequest(
        @NotNull(message = "User ID is required")
        Long userId,

        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must be at most 255 characters")
        String title,

        String description,

        @NotBlank(message = "Instructions are required")
        String instructions,

        Integer servings,

        Integer prepTimeMinutes,

        Integer cookTimeMinutes,

        @Size(max = 500, message = "URL must be at most 500 characters")
        String imageUrl
) {
}
