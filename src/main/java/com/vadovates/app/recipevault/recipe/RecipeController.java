package com.vadovates.app.recipevault.recipe;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recipes")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public List<RecipeDto> getAll() {
        return recipeService.findAll()
                .stream()
                .map(RecipeDto::from)
                .toList();
    }

    @GetMapping("/{id}")
    public RecipeDto getById(@PathVariable Long id) {
        return RecipeDto.from(recipeService.findById(id));
    }

    @GetMapping("/search")
    public List<RecipeDto> search (@RequestParam String q) {
        return recipeService.search(q)
                .stream()
                .map(RecipeDto::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecipeDto create(@Valid @RequestBody CreateRecipeRequest recipeRequest) {
        return RecipeDto.from(recipeService.create(recipeRequest));
    }

    @PutMapping("/{id}")
    public RecipeDto update(@PathVariable Long id, @Valid @RequestBody CreateRecipeRequest recipeRequest) {
        return RecipeDto.from(recipeService.update(id, recipeRequest));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete (@PathVariable Long id) {
        recipeService.delete(id);
    }

}
