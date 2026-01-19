package com.vadovates.app.recipevault.ingredient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findByName(String name);
    List<Ingredient> findByCategory(String category);
    List<Ingredient> findByNameContainingIgnoreCase(String name);
}
