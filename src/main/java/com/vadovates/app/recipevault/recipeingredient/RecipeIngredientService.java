package com.vadovates.app.recipevault.recipeingredient;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class RecipeIngredientService {
    private final RecipeIngredientRepository recipeIngredientRepository;

    public RecipeIngredientService(RecipeIngredientRepository recipeIngredientRepository) {
        this.recipeIngredientRepository = recipeIngredientRepository;
    }

    public List<RecipeIngredient> findAll() {
        return recipeIngredientRepository.findAll();
    }

    public RecipeIngredient findById(Long id) {
        return recipeIngredientRepository.findById(id)
                .orElseThrow(() -> new RecipeIngredientNotFoundException(id));
    }

    public List<RecipeIngredient> findByRecipe(Long recipeId) {
        return recipeIngredientRepository.findByRecipeId(recipeId);
    }

    public List<RecipeIngredient> findByIngredient(Long ingredientId) {
        return recipeIngredientRepository.findByIngredientId(ingredientId);
    }

    @Transactional
    public RecipeIngredient create (Long recipeId, CreateRecipeIngredientRequest recipeIngredientRequest) {
        recipeIngredientRepository.findByRecipeIdAndIngredientId(recipeId,
                recipeIngredientRequest.ingredientId())
                .ifPresent(existing -> {
                    throw new RecipeIngredientAlreadyExistsException(recipeId,
                            recipeIngredientRequest.ingredientId());
                });

        RecipeIngredient recipeIngredient = new RecipeIngredient(
                recipeId,
                recipeIngredientRequest.ingredientId(),
                recipeIngredientRequest.amount(),
                recipeIngredientRequest.unit(),
                recipeIngredientRequest.optional()
        );

        return recipeIngredientRepository.save(recipeIngredient);
    }

    @Transactional
    public RecipeIngredient update(Long id, CreateRecipeIngredientRequest recipeIngredientRequest) {
        RecipeIngredient recipeIngredient = findById(id);
        recipeIngredient.setAmount(recipeIngredientRequest.amount());
        recipeIngredient.setUnit(recipeIngredientRequest.unit());
        recipeIngredient.setOptional(recipeIngredientRequest.optional());
        return recipeIngredientRepository.save(recipeIngredient);
    }

    @Transactional
    public void delete(Long id) {
        if (!recipeIngredientRepository.existsById(id)) {
            throw new RecipeIngredientNotFoundException(id);
        }
        recipeIngredientRepository.deleteById(id);
    }
}
