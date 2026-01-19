package com.vadovates.app.recipevault.recipe;

import java.time.Instant;

public record RecipeDto(
        Long id,
        Long userId,
        String title,
        String description,
        String instructions,
        Integer servings,
        Integer prepTimeMinutes,
        Integer cookTimeMinutes,
        String imageUrl,
        Instant createdAt,
        Instant updatedAt
) {
    public static RecipeDto from (Recipe recipe) {
        return new RecipeDto(
                recipe.getId(),
                recipe.getUserId(),
                recipe.getTitle(),
                recipe.getDescription(),
                recipe.getInstructions(),
                recipe.getServings(),
                recipe.getPrepTimeMinutes(),
                recipe.getCookTimeMinutes(),
                recipe.getImageUrl(),
                recipe.getCreatedAt(),
                recipe.getUpdatedAt()
        );
    }
}
