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
    }

    @Test
    void shouldReturn409WhenIngredientExists() {
        ingredientRepository.save(new Ingredient("Salt", "Spices"));

        String json = """
                {
                    "name": "Salt",
                    "category": "Spices"
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
        ingredientRepository.save(new Ingredient("Sugar", "Baking"));

        ResponseEntity<IngredientDto[]> response = restClient()
                .get()
                .uri("/api/ingredients")
                .retrieve()
                .toEntity(IngredientDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }
}