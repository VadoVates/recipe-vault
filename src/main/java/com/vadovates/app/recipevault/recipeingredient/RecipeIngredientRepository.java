package com.vadovates.app.recipevault.recipeingredient;

import com.vadovates.app.recipevault.ingredient.IngredientRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {
    List<RecipeIngredient> findByRecipeId(Long recipeId);
    Optional<RecipeIngredient> findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);
    List<RecipeIngredient> findByIngredientId(Long ingredientId);
    void deleteByRecipeId(Long recipeId);
}
