package com.vadovates.app.recipevault.recipeingredient;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateRecipeIngredientRequest(
        @NotNull (message = "Ingredient ID is required")
        Long ingredientId,

        BigDecimal amount,
        @Size(max = 30, message = "Unit must be at most 30 characters")
        String unit,

        boolean optional
) {
}
