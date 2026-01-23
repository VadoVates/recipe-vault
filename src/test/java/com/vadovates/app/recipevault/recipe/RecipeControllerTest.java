package com.vadovates.app.recipevault.recipe;

import com.vadovates.app.recipevault.BaseIntegrationTest;
import com.vadovates.app.recipevault.user.User;
import com.vadovates.app.recipevault.user.UserDto;
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
/*
    @Test
    void shouldCreateRecipe() {
        User user = userRepository.save(new User (""))
        String json = """
                {   
                    "userId": "test@example.com",
                    "password": "secret123",
                    "displayName": "Test User"
                }
                """;

        ResponseEntity<UserDto> response = restClient()
                .post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .body(json)
                .retrieve()
                .toEntity(UserDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().email()).isEqualTo("test@example.com");
        assertThat(response.getBody().displayName()).isEqualTo("Test User");
        assertThat(response.getBody().id()).isNotNull();
    }

    @Test
    void shouldReturn409WhenUserExists() {
        userRepository.save(new User("existing@example.com", "hash", "Existing"));

        String json = """
                {
                    "email": "existing@example.com",
                    "password": "password",
                    "displayName": "Another"
                }
                """;

        assertThatThrownBy(() ->
                restClient()
                        .post()
                        .uri("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(json)
                        .retrieve()
                        .toEntity(String.class)
        ).isInstanceOf(HttpClientErrorException.Conflict.class);
    }


    @Test
    void shouldReturn404WhenUserNotFound() {
        assertThatThrownBy(() ->
                restClient()
                        .get()
                        .uri("/api/users/9999")
                        .retrieve()
                        .toEntity(String.class)
        ).isInstanceOf(HttpClientErrorException.NotFound.class);
    }

    @Test
    void shouldReturnAllUsers() {
        userRepository.save(new User("user1@test.com", "hash", "User One"));

        ResponseEntity<UserDto[]> response = restClient()
                .get()
                .uri("/api/users")
                .retrieve()
                .toEntity(UserDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }
    */
}
