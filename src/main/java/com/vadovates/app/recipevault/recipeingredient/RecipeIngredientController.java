package com.vadovates.app.recipevault.recipeingredient;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recipes/{recipeId}/ingredients")
public class RecipeIngredientController {
    private final RecipeIngredientService recipeIngredientService;
    public RecipeIngredientController(RecipeIngredientService recipeIngredientService) {
        this.recipeIngredientService = recipeIngredientService;
    }

    @GetMapping
    public List<RecipeIngredientDto> getByRecipe(@PathVariable Long recipeId) {
        return recipeIngredientService.findByRecipe(recipeId)
            .stream()
            .map(RecipeIngredientDto::from)
            .toList();
    }

    @GetMapping("/{id}")
    public RecipeIngredientDto getById(@PathVariable Long id) {
        return RecipeIngredientDto.from(recipeIngredientService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeIngredientDto create (@PathVariable Long recipeId,
                                       @Valid @RequestBody CreateRecipeIngredientRequest recipeIngredientRequest) {
        return RecipeIngredientDto.from(recipeIngredientService.create(recipeId, recipeIngredientRequest));
    }

    @PutMapping("/{id}")
    public RecipeIngredientDto update (@PathVariable Long id,
                                       @Valid @RequestBody CreateRecipeIngredientRequest recipeIngredientRequest) {
        return RecipeIngredientDto.from(recipeIngredientService.update(id, recipeIngredientRequest));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Long id) {
        recipeIngredientService.delete(id);
    }

}
