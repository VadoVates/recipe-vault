package com.vadovates.app.recipevault.recipeingredient;

public class RecipeIngredientNotFoundException extends RuntimeException {
    public RecipeIngredientNotFoundException(Long id) {
        super("Ingredient with id: " + id + " not found");
    }
}
