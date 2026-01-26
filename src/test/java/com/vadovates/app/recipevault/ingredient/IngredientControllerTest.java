package com.vadovates.app.recipevault.ingredient;

import com.vadovates.app.recipevault.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class IngredientControllerTest extends BaseIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private IngredientRepository ingredientRepository;

    private RestClient restClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void shouldCreateIngredient() {
        String json = """
                {
                    "name": "Pepper",
                    "category": "Vegetables"
                }
                """;

        ResponseEntity<IngredientDto> response = restClient()
                .post()
                .uri("/api/ingredients")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .toEntity(IngredientDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo("Pepper");
        assertThat(response.getBody().category()).isEqualTo("Vegetables");
        assertThat(response.getBody().createdAt()).isNotNull();
    }

    @Test
    void shouldReturn409WhenIngredientExists() {
        ingredientRepository.save(new Ingredient("Salt", "Spices"));

        String json = """
                {
                    "name": "Salt",
                    "category": "Vegetables"
                }
                """;

        assertThatThrownBy(() ->
                restClient()
                        .post()
                        .uri("/api/ingredients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(json)
                        .retrieve()
                        .toEntity(String.class)
        ).isInstanceOf(HttpClientErrorException.Conflict.class);
    }

    @Test
    void shouldReturn404WhenIngredientNotFound() {
        assertThatThrownBy(() ->
                restClient()
                        .get()
                        .uri("/api/ingredients/9999")
                        .retrieve()
                        .toEntity(String.class)
        ).isInstanceOf(HttpClientErrorException.NotFound.class);
    }

    @Test
    void shouldReturnAllIngredients() {
        Ingredient ingredient1 = ingredientRepository.save(new Ingredient("Sugar", "Baking"));
        Ingredient ingredient2 = ingredientRepository.save(new Ingredient("Salt", "Spices"));
        Ingredient ingredient3 = ingredientRepository.save(new Ingredient("Pepper", "Vegetables"));

        ResponseEntity<IngredientDto[]> response = restClient()
                .get()
                .uri("/api/ingredients")
                .retrieve()
                .toEntity(IngredientDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
        assertThat(response.getBody()).hasSize(3);
        assertThat(response.getBody()).contains(IngredientDto.from(ingredient1), IngredientDto.from(ingredient2),
                IngredientDto.from(ingredient3));
    }

    @Test
    void shouldReturnIngredientById() {
        Ingredient ingredient1 = ingredientRepository.save(new Ingredient("Salt", "Spices"));
        Ingredient ingredient2 = ingredientRepository.save(new Ingredient("Pepper", "Vegetables"));

        ResponseEntity<IngredientDto> response = restClient()
                .get()
                .uri("/api/ingredients/%d".formatted(ingredient1.getId()))
                .retrieve()
                .toEntity(IngredientDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(ingredient1.getId());
        assertThat(response.getBody().name()).isEqualTo("Salt");
        assertThat(response.getBody().category()).isEqualTo("Spices");
        assertThat(response.getBody()).isNotEqualTo(IngredientDto.from(ingredient2));
    }

    @Test
    void shouldReturnIngredientsByCategory() {
        Ingredient ingredient1 = ingredientRepository.save(new Ingredient("Salt", "Spices"));
        Ingredient ingredient2 = ingredientRepository.save(new Ingredient("Pepper", "Spices"));
        Ingredient ingredient3 = ingredientRepository.save(new Ingredient("Sugar", "Baking"));

        ResponseEntity<IngredientDto[]> response = restClient()
                .get()
                .uri("/api/ingredients?category=Spices")
                .retrieve()
                .toEntity(IngredientDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()).contains(IngredientDto.from(ingredient1), IngredientDto.from(ingredient2));
        assertThat(response.getBody()).doesNotContain(IngredientDto.from(ingredient3));
    }

    @Test
    void shouldDeleteIngredient() {
        Ingredient ingredient1 = ingredientRepository.save(new Ingredient("Salt", "Spices"));
        Ingredient ingredient2 = ingredientRepository.save(new Ingredient("Pepper", "Vegetables"));

        ResponseEntity<Void> response = restClient()
                .delete()
                .uri("/api/ingredients/%d".formatted(ingredient1.getId()))
                .retrieve()
                .toBodilessEntity();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(ingredientRepository.findById(ingredient1.getId())).isEmpty();
        assertThat(ingredientRepository.findById(ingredient2.getId())).isPresent();
    }

    @Test
    void shouldUpdateIngredient() {
        Ingredient ingredient = ingredientRepository.save(new Ingredient("Salt", "Spices"));

        String json = """
                {
                    "name": "Pepper",
                    "category": "Vegetables"
                }
                """;

        ResponseEntity<IngredientDto> response = restClient()
                .put()
                .uri("/api/ingredients/%d".formatted(ingredient.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .toEntity(IngredientDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(ingredient.getId());
        assertThat(response.getBody().name()).isEqualTo("Pepper");
        assertThat(response.getBody().category()).isEqualTo("Vegetables");
        assertThat(response.getBody().createdAt()).isEqualTo(ingredient.getCreatedAt());
    }
}