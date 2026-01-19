package com.vadovates.app.recipevault.ingredient;

public class IngredientNotFoundException extends RuntimeException{
    public IngredientNotFoundException(Long id) {
        super("Ingredient with id: " + id + " not found.");
    }
}
