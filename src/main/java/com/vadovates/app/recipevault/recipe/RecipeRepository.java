package com.vadovates.app.recipevault.recipe;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    List<Recipe> findByUserId (Long userId);
    List<Recipe> findByTitleContainingIgnoreCase (String title);
    Optional<Recipe> findByTitleAndUserId (String title, Long userId);
}
