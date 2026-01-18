package com.vadovates.app.recipevault.ingredient;

import java.time.Instant;

public record IngredientDto(
        Long id,
        String name,
        String category,
        Instant createdAt
) {
    public static IngredientDto from(Ingredient ingredient) {
        return new IngredientDto(
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getCategory(),
                ingredient.getCreatedAt()
        );
    }
}
