package com.vadovates.app.recipevault.ingredient;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/ingredients")
public class IngredientController {
    private final IngredientService ingredientService;

    public IngredientController(IngredientService ingredientService) {
        this.ingredientService = ingredientService;
    }

    @GetMapping
    public List<IngredientDto> getAll() {
        return ingredientService.findAll()
                .stream()
                .map(IngredientDto::from)
                .toList();
    }

    @GetMapping("/{id}")
    public IngredientDto getById(@PathVariable Long id) {
        return IngredientDto.from(ingredientService.findById(id));
    }

    @GetMapping("/search")
    public List<IngredientDto> search(@RequestParam String q) {
        return ingredientService.search(q)
                .stream()
                .map(IngredientDto::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IngredientDto create(@Valid @RequestBody CreateIngredientRequest ingredientRequest) {
        return IngredientDto.from(ingredientService.create(ingredientRequest));
    }

    @PutMapping("/{id}")
    public IngredientDto update (@PathVariable Long id, @Valid @RequestBody CreateIngredientRequest ingredientRequest) {
        return IngredientDto.from(ingredientService.update(id, ingredientRequest));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        ingredientService.delete(id);
    }

}
