package com.vadovates.app.recipevault.recipe;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public RecipeService(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }
    
    public List<Recipe> findAll() {
        return recipeRepository.findAll();
    }

    public Recipe findById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RecipeNotFoundException(id));
    }

    public List<Recipe> search(String q) {
        return recipeRepository.findByTitleContainingIgnoreCase(q);
    }

    public List<Recipe> findByUserId (Long userId) {
        return recipeRepository.findByUserId(userId);
    }

    @Transactional
    public Recipe create (CreateRecipeRequest recipeRequest) {
        recipeRepository.findByTitleAndUserId(recipeRequest.title(), recipeRequest.userId()).ifPresent(existing -> {
            throw new RecipeAlreadyExistsException(recipeRequest.title());
        });

        Recipe recipe = new Recipe (
                recipeRequest.userId(),
                recipeRequest.title(),
                recipeRequest.description(),
                recipeRequest.instructions(),
                recipeRequest.servings(),
                recipeRequest.prepTimeMinutes(),
                recipeRequest.cookTimeMinutes(),
                recipeRequest.imageUrl()
                );
        return recipeRepository.save(recipe);
    }

    @Transactional
    public Recipe update (Long id, CreateRecipeRequest recipeRequest) {
        Recipe recipe = findById(id);
        recipe.setUserId(recipeRequest.userId());
        recipe.setTitle(recipeRequest.title());
        recipe.setDescription(recipeRequest.description());
        recipe.setInstructions(recipeRequest.instructions());
        recipe.setServings(recipeRequest.servings());
        recipe.setPrepTimeMinutes(recipeRequest.prepTimeMinutes());
        recipe.setCookTimeMinutes(recipeRequest.cookTimeMinutes());
        recipe.setImageUrl(recipeRequest.imageUrl());
        recipe.setUpdatedAt();

        return recipeRepository.save(recipe);
    }

    @Transactional
    public void delete (Long id) {
        if (!recipeRepository.existsById(id)) {
            throw new RecipeNotFoundException(id);
        }
        recipeRepository.deleteById(id);
    }
}
