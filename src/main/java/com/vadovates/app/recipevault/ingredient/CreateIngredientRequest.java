package com.vadovates.app.recipevault.ingredient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateIngredientRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must by at most 100 characters")
        String name,

        @Size(max = 50, message = "Category must be at most 50 characters")
        String category
) {
}
