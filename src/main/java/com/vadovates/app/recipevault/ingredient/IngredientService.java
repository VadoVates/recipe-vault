package com.vadovates.app.recipevault.ingredient;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class IngredientService {
    private final IngredientRepository ingredientRepository;
    public IngredientService(IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }
    public List<Ingredient> findAll() {
        return ingredientRepository.findAll();
    }

    public Ingredient findById(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new IngredientNotFoundException(id));
    }

    public List<Ingredient> search (String q) {
        return ingredientRepository.findByNameContainingIgnoreCase(q);
    }

    public List<Ingredient> findByCategory(String category) {
        return ingredientRepository.findByCategory(category);
    }

    @Transactional
    public Ingredient create(CreateIngredientRequest ingredientRequest) {
        ingredientRepository.findByName(ingredientRequest.name()).ifPresent(existing -> {
            throw new IngredientAlreadyExistsException(ingredientRequest.name());
        });

        Ingredient ingredient = new Ingredient(ingredientRequest.name(), ingredientRequest.category());
        return ingredientRepository.save(ingredient);
    }

    @Transactional
    public Ingredient update(Long id, CreateIngredientRequest ingredientRequest) {
        Ingredient ingredient = findById(id);
        ingredient.setName(ingredientRequest.name());
        ingredient.setCategory(ingredientRequest.category());
        return ingredientRepository.save(ingredient);
    }

    @Transactional
    public void delete(Long id) {
        if (!ingredientRepository.existsById(id)) {
            throw new IngredientNotFoundException(id);
        }
        ingredientRepository.deleteById(id);
    }
}
