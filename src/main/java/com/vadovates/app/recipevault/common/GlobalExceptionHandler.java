package com.vadovates.app.recipevault.common;

import com.vadovates.app.recipevault.ingredient.IngredientAlreadyExistsException;
import com.vadovates.app.recipevault.ingredient.IngredientNotFoundException;
import com.vadovates.app.recipevault.recipe.RecipeAlreadyExistsException;
import com.vadovates.app.recipevault.recipe.RecipeNotFoundException;
import com.vadovates.app.recipevault.recipeingredient.RecipeIngredientAlreadyExistsException;
import com.vadovates.app.recipevault.recipeingredient.RecipeIngredientNotFoundException;
import com.vadovates.app.recipevault.user.UserAlreadyExistsException;
import com.vadovates.app.recipevault.user.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            IngredientNotFoundException.class,
            RecipeNotFoundException.class,
            RecipeIngredientNotFoundException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler({
            IngredientAlreadyExistsException.class,
            RecipeAlreadyExistsException.class,
            RecipeIngredientAlreadyExistsException.class,
            UserAlreadyExistsException.class
    })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed.", errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred: " + ex.getMessage() + ".");
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message) {
        return buildResponse(status, message, null);
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String message,
                                                        Map<String, String> errors) {
        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                errors
        );

        return ResponseEntity.status(status).body(response);
    }
}
