package com.vadovates.app.recipevault.recipe;

public class RecipeAlreadyExistsException extends RuntimeException {
    public RecipeAlreadyExistsException(String title) {
        super("Recipe with title: " + title + " already exists.");
    }
}
