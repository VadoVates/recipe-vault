package com.vadovates.app.recipevault.recipeingredient;

public class RecipeIngredientAlreadyExistsException extends RuntimeException {
    public RecipeIngredientAlreadyExistsException(Long recipeId, Long ingredientId) {
        super("Ingredient: " + ingredientId + " already exists in recipe: " + recipeId);
    }
}
