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
    private final IngredientService service;

    public IngredientController(IngredientService service) {
        this.service = service;
    }

    @GetMapping
    public List<IngredientDto> getAll() {
        return service.findAll()
                .stream()
                .map(IngredientDto::from)
                .toList();
    }

    @GetMapping("/{id}")
    public IngredientDto getById(@PathVariable Long id) {
        return IngredientDto.from(service.findById(id));
    }

    @GetMapping("/search")
    public List<IngredientDto> search(@RequestParam String query) {
        return service.search(query)
                .stream()
                .map(IngredientDto::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IngredientDto create(@Valid @RequestBody CreateIngredientRequest request) {
        return IngredientDto.from(service.create(request));
    }

    @PutMapping("/{id}")
    public IngredientDto update (@PathVariable Long id, @Valid @RequestBody CreateIngredientRequest request) {
        return IngredientDto.from(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @ExceptionHandler(IngredientNotFoundException.class)
    public ResponseEntity<String> handleNotFound(IngredientNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IngredientAlreadyExistsException.class)
    public ResponseEntity<String> handleAlreadyExists(IngredientAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
