package com.vadovates.app.recipevault.ingredient;

import com.vadovates.app.recipevault.BaseIntegrationTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.empty;

public class IngredientControllerTest extends BaseIntegrationTest {
    @Autowired
    private IngredientRepository ingredientRepository;

    @Test
    public void shouldCreateIngredient() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "name": "Pepper",
                            "category": "Vegetables"
                        }
                        """)
                .when()
                    .post("/api/ingredients")
                .then()
                    .statusCode(201)
                    .body("name", equalTo("Pepper"))
                    .body("category", equalTo("Vegetables"))
                    .body("id", notNullValue());
    }

    @Test
    public void shouldReturn409WhenIngredientExists() {
        ingredientRepository.save(new Ingredient("Salt", "Spices"));
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {
                            "name": "Salt",
                            "category": "Spices"
                        }
                        """)
                .when()
                .post("/api/ingredients")
                .then()
                .statusCode(409)
                .body("message", containsString("already exists"));
    }

    @Test
    public void shouldReturn404WhenIngredientNotFound() {
        given()
                .when()
                .get("/api/ingredients/9999")
                .then()
                .statusCode(404)
                .body("message", containsString("not found"));
    }

    @Test
    public void shouldReturnAllIngredients() {
        // Given
        ingredientRepository.save(new Ingredient("Sugar", "Baking"));

        // When & Then
        given()
                .when()
                .get("/api/ingredients")
                .then()
                .statusCode(200)
                .body("$", not(empty()));
    }

    @Test
    public void shouldReturn400WhenNameIsBlank() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                    "name": "",
                    "category": "Test"
                }
                """)
                .when()
                .post("/api/ingredients")
                .then()
                .statusCode(400)
                .body("details.name", notNullValue());
    }
}
