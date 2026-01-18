package com.vadovates.app.recipevault.ingredient;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class IngredientService {
    private final IngredientRepository repository;
    public IngredientService(IngredientRepository repository) {
        this.repository = repository;
    }
    public List<Ingredient> findAll() {
        return repository.findAll();
    }

    public Ingredient findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IngredientNotFoundException(id));
    }

    public List<Ingredient> search (String query) {
        return repository.findByNameContainingIgnoreCase(query);
    }

    @Transactional
    public Ingredient create(CreateIngredientRequest request) {
        repository.findByName(request.name()).ifPresent(existing -> {
            throw new IngredientAlreadyExistsException(request.name());
        });

        Ingredient ingredient = new Ingredient(request.name(), request.category());
        return repository.save(ingredient);
    }

    @Transactional
    public Ingredient update(Long id, CreateIngredientRequest request) {
        Ingredient ingredient = findById(id);
        ingredient.setName(request.name());
        ingredient.setCategory(request.category());
        return repository.save(ingredient);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new IngredientNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
