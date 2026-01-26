package com.vadovates.app.recipevault.recipe;

import com.vadovates.app.recipevault.BaseIntegrationTest;
import com.vadovates.app.recipevault.user.User;
import com.vadovates.app.recipevault.user.UserRepository;
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

public class RecipeControllerTest extends BaseIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    private RestClient restClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void shouldCreateRecipe() {
        User user = userRepository.save(new User("chef@test.com", "hash", "Chef"));

        String json = """
                {
                    "userId": %d,
                    "title": "Spaghetti",
                    "description": "Simple spaghetti recipe",
                    "instructions": "Boil pasta, add sauce",
                    "servings": 4,
                    "prepTimeMinutes": 15,
                    "cookTimeMinutes": 20,
                    "imageUrl": "http://localhost/image.jpg"
                }
                """.formatted(user.getId());

        ResponseEntity<RecipeDto> response = restClient()
                .post()
                .uri("/api/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .toEntity(RecipeDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isNotNull();
        assertThat(response.getBody().userId()).isEqualTo(user.getId());
        assertThat(response.getBody().title()).isEqualTo("Spaghetti");
        assertThat(response.getBody().description()).isEqualTo("Simple spaghetti recipe");
        assertThat(response.getBody().instructions()).isEqualTo("Boil pasta, add sauce");
        assertThat(response.getBody().servings()).isEqualTo(4);
        assertThat(response.getBody().prepTimeMinutes()).isEqualTo(15);
        assertThat(response.getBody().cookTimeMinutes()).isEqualTo(20);
        assertThat(response.getBody().imageUrl()).isEqualTo("http://localhost/image.jpg");
    }

    @Test
    void shouldReturn404WhenRecipeNotFound() {
        assertThatThrownBy(() ->
                restClient()
                        .get()
                        .uri("/api/recipes/9999")
                        .retrieve()
                        .toEntity(String.class)
        ).isInstanceOf(HttpClientErrorException.NotFound.class);
    }

    @Test
    void shouldReturnAllRecipes() {
        User user = userRepository.save(new User("user1@test.com", "hash", "User One"));
        recipeRepository.save(new Recipe(user.getId(), "Pizza", "Easy pizza recipe",
                "Put tomato sauce on the dough and put it into the oven",
                2, 60, 30, "https://server.com/img.png"));

        ResponseEntity<RecipeDto[]> response = restClient()
                .get()
                .uri("/api/recipes")
                .retrieve()
                .toEntity(RecipeDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    void shouldReturn409WhenDuplicateRecipeForSameUser() {
        User user = userRepository.save(new User("existing@example.com", "hash", "Existing"));
        recipeRepository.save(new Recipe(user.getId(), "Pasta", null, "Boil pasta",
                4, 5, 30, null));
        String json = """
                {
                    "userId": %d,
                    "title": "Pasta",
                    "instructions": "Totally different instructions",
                    "servings": 2
                }
                """.formatted(user.getId());

        assertThatThrownBy(() ->
                restClient()
                        .post()
                        .uri("/api/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(json)
                        .retrieve()
                        .toEntity(String.class)
        ).isInstanceOf(HttpClientErrorException.Conflict.class);
    }
}