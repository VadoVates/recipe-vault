package com.vadovates.app.recipevault.bdd.steps;

import com.vadovates.app.recipevault.ingredient.Ingredient;
import com.vadovates.app.recipevault.ingredient.IngredientDto;
import com.vadovates.app.recipevault.ingredient.IngredientRepository;
import com.vadovates.app.recipevault.user.User;
import com.vadovates.app.recipevault.user.UserDto;
import io.cucumber.java.Before;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;

public class UserSteps {
    @LocalServerPort
    private int port;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ResponseEntity<?> response;
    private HttpStatus errorStatus;
    private User savedUser;

    private RestClient restClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Before
    public void cleanup() {
        jdbcTemplate.execute("TRUNCATE TABLE users RESTART IDENTITY CASCADE");
    }

    @Given("the users database is empty")
    public void theUsersDatabaseIsEmpty() {
        assertThat(ingredientRepository.findAll()).isEmpty();
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int status) {
        if (errorStatus != null) {
            assertThat(errorStatus.value()).isEqualTo(status);
        } else {
            assertThat(response.getStatusCode().value()).isEqualTo(status);
        }
    }

    @When("I create the user with email {string} and password {string} and display name {string}")
    public void iCreateTheUser(String email, String password, String displayName) {
        String json = """
                {
                "email": "%s",
                "password": "%s",
                "displayName": "%s"
                }
                """.formatted(email, password, displayName);

        try {
            response = restClient()
                    .post()
                    .uri("/api/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .retrieve()
                    .toEntity(UserDto.class);
            errorStatus = null;
        } catch (HttpClientErrorException e) {
            errorStatus = HttpStatus.valueOf(e.getStatusCode().value());
            response = null;
        }
    }


    @And("the response should contain user with name {string}")
    public void theResponseShouldContainUserWithName(String name) {
        assertThat(response.getBody()).isNotNull();
        UserDto dto = (UserDto) response.getBody();
        assertThat(dto.displayName()).isEqualTo(name);
    }
}
