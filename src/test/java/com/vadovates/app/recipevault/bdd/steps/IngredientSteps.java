package com.vadovates.app.recipevault.bdd.steps;

import com.vadovates.app.recipevault.ingredient.Ingredient;
import com.vadovates.app.recipevault.ingredient.IngredientDto;
import com.vadovates.app.recipevault.ingredient.IngredientRepository;
import io.cucumber.java.Before;
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

public class IngredientSteps {
    @LocalServerPort
    private int port;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ResponseEntity<?> response;
    private HttpStatus errorStatus;
    private Ingredient savedIngredient;

    private RestClient restClient() {
        return RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Before
    public void cleanup() {
        jdbcTemplate.execute("TRUNCATE TABLE ingredients RESTART IDENTITY CASCADE");
    }

    @Given("the ingredients database is empty")
    public void theIngredientDatabaseIsEmpty() {
        assertThat(ingredientRepository.findAll()).isEmpty();
    }

    @Given("an ingredient with name {string} and category {string} exists")
    public void anIngredientWithNameAndCategoryExists(String name, String category) {
        savedIngredient = ingredientRepository.save(new Ingredient(name, category));
    }

    @When("I create an ingredient with name {string} and category {string}")
    public void iCreateAnIngredient(String name, String category) {
        String json = """
                {
                "name": "%s",
                "category": "%s"
                }
                """.formatted(name, category);

        try {
            response = restClient()
                    .post()
                    .uri("/api/ingredients")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(json)
                    .retrieve()
                    .toEntity(IngredientDto.class);
            errorStatus = null;
        } catch (HttpClientErrorException e) {
            errorStatus = HttpStatus.valueOf(e.getStatusCode().value());
            response = null;
        }
    }

    @When("I request the ingredient by its ID")
    public void iRequestTheIngredientByItsID() {
        response = restClient()
                .get()
                .uri("/api/ingredients/" + savedIngredient.getId())
                .retrieve()
                .toEntity(IngredientDto.class);
    }

    @When("I request ingredient with ID {int}")
    public void iRequestIngredientWithID(int id) {
        try {
            response = restClient()
                    .get()
                    .uri("/api/ingredients/" + id)
                    .retrieve()
                    .toEntity(IngredientDto.class);
            errorStatus = null;
        } catch (HttpClientErrorException e) {
            errorStatus = HttpStatus.valueOf(e.getStatusCode().value());
            response = null;
        }
    }

    @Then("the response status should be {int}")
    public void theResponseStatusShouldBe(int status) {
        if (errorStatus != null) {
            assertThat(errorStatus.value()).isEqualTo(status);
        } else {
            assertThat(response.getStatusCode().value()).isEqualTo(status);
        }
    }

    @And("the response should contain ingredient with name {string}")
    public void theResponseShouldContainIngredientWithName(String name) {
        assertThat(response.getBody()).isNotNull();
        IngredientDto dto = (IngredientDto) response.getBody();
        assertThat(dto.name()).isEqualTo(name);
    }
}
