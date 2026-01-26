package com.vadovates.app.recipevault.recipe;

import com.vadovates.app.recipevault.BaseIntegrationTest;
import com.vadovates.app.recipevault.user.User;
import com.vadovates.app.recipevault.user.UserNotFoundException;
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
        User user = userRepository.save(new User("user1@test.com", "hash", "User1"));

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
        assertThat(response.getBody().createdAt()).isNotNull();
        assertThat(response.getBody().updatedAt()).isNotNull();
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
        User user = userRepository.save(new User("user2@test.com", "hash", "User2"));
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
        User user = userRepository.save(new User("user3@test.com", "hash", "User3"));
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

    @Test
    void shouldUpdateRecipeAndChangeUpdatedAt() {
        User user = userRepository.save(new User("user4@test.com", "hash", "User4"));
        Recipe recipe = recipeRepository.save(new Recipe(user.getId(), "Carbonara", "Another pasta",
                "Boil pasta, quick", 2, 15, 30, null));

        String json = """
                        {
                            "userId": %d,
                            "title": "Carbonara",
                            "description": "Another pasta recipe",
                            "instructions": "Boil pasta, quick, add sauce and yolk",
                            "servings": 3,
                            "prepTimeMinutes": 10,
                            "cookTimeMinutes": 15,
                            "imageUrl": "http://localhost/image.jpg"
                        }
                """.formatted(user.getId());

        ResponseEntity<RecipeDto> response = restClient()
                .put()
                .uri("/api/recipes/%d".formatted(recipe.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .toEntity(RecipeDto.class);

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().id()).isEqualTo(recipe.getId());
        assertThat(response.getBody().userId()).isEqualTo(user.getId());
        assertThat(response.getBody().description()).isEqualTo("Another pasta recipe");
        assertThat(response.getBody().instructions()).isEqualTo("Boil pasta, quick, add sauce and yolk");
        assertThat(response.getBody().servings()).isEqualTo(3);
        assertThat(response.getBody().prepTimeMinutes()).isEqualTo(10);
        assertThat(response.getBody().cookTimeMinutes()).isEqualTo(15);
        assertThat(response.getBody().imageUrl()).isEqualTo("http://localhost/image.jpg");
        assertThat(response.getBody().createdAt()).isEqualTo(recipe.getCreatedAt());
        assertThat(response.getBody().updatedAt()).isAfterOrEqualTo(recipe.getUpdatedAt());
    }

    @Test
    void shouldReturnAllRecipesForUser() {
        User user1 = userRepository.save(new User("user@test.com", "hash", "User"));
        User user2 = userRepository.save(new User("user2@test.com", "hash", "User2"));

        Recipe recipe1 = recipeRepository.save(new Recipe(user1.getId(), "Carbonara", "Quick carbonara",
                "Boil pasta, add sauce", 2, 10, 15, null));
        Recipe recipe2 = recipeRepository.save(new Recipe(user2.getId(), "Pasta", "Another pasta",
                "Boil pasta", 4, 5, 30, null));
        Recipe recipe3 = recipeRepository.save(new Recipe(user1.getId(), "Spaghetti", "Spaghetti with tomato sauce",
                "Boil spaghetti, add sauce", 3, 8, 12, null));

        ResponseEntity<RecipeDto[]> response = restClient()
                .get()
                .uri("/api/recipes/user/%d".formatted(user1.getId()))
                .retrieve()
                .toEntity(RecipeDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()).contains(RecipeDto.from(recipe1), RecipeDto.from(recipe3));
        assertThat(response.getBody()).doesNotContain(RecipeDto.from(recipe2));
    }

    @Test
    void shouldReturnRecipeForUser() {
        User user1 = userRepository.save(new User("user@test.com", "hash", "User"));

        Recipe recipe1 = recipeRepository.save(new Recipe(user1.getId(), "Carbonara", "Quick carbonara",
                "Boil pasta, add sauce", 2, 10, 15, null));

        ResponseEntity<RecipeDto> response = restClient()
                .get()
                .uri("/api/recipes/user/{userId}/{recipeId}", user1.getId(), recipe1.getId())
                .retrieve()
                .toEntity(RecipeDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(recipe1.getId());
    }

//    @Test
//    void shouldDeleteRecipe() {
//        User user1 = userRepository.save(new User("user1@test.com", "hash", "User"));
//        Recipe recipe1 = recipeRepository.save(new Recipe(user1.getId(), "Carbonara", "Quick carbonara",
//                "Boil pasta, add sauce", 2, 10, 15, null));
//        recipeRepository.save(new Recipe(user1.getId(), "Spaghetti", "quick spaghetti",
//                "Boil spaghetti, add sauce", 3, 8, 12, null));
//
//                ResponseEntity<Void> response = restClient()
//                        .delete()
//                        .uri("/api/recipes/%d".formatted(recipe1.getId()))
//                        .retrieve()
//                        .toBodilessEntity();
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
//        assertThat(recipeRepository.findById(recipe1.getId())).isEmpty();
//
//        assertThatThrownBy(() -> restClient()
//                .get()
//                .uri("/api/recipes/%d".formatted(recipe1.getId()))
//                .retrieve()
//                .toEntity(RecipeDto.class)
//        ).isInstanceOf(UserNotFoundException.class);
//    }
}