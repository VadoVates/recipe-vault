package com.vadovates.app.recipevault.recipeingredient;

import java.math.BigDecimal;

public record RecipeIngredientDto(
        Long id,
        Long recipeId,
        Long ingredientId,
        BigDecimal amount,
        String unit,
        boolean optional
) {
    public static RecipeIngredientDto from(RecipeIngredient recipeIngredient) {
        return new RecipeIngredientDto(
            recipeIngredient.getId(),
            recipeIngredient.getRecipeId(),
            recipeIngredient.getIngredientId(),
            recipeIngredient.getAmount(),
            recipeIngredient.getUnit(),
            recipeIngredient.isOptional()
        );
    }
}
