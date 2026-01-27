Feature: Ingredient Management
  As a user of RecipeVault
  I want to manage ingredients
  So that I can use them in my recipes

  Scenario: Create a new ingredient
    Given the ingredients database is empty
    When I create an ingredient with name "Tomato" and category "Vegetables"
    Then the response status should be 201
    And the response should contain ingredient with name "Tomato"
    
  Scenario: Cannot create duplicate ingredient
    Given an ingredient with name "Salt" and category "Spices" exists
    When I create an ingredient with name "Salt" and category "Spices"
    Then the response status should be 409

  Scenario: Get ingredient by ID
    Given an ingredient with name "Pepper" and category "Spices" exists
    When I request the ingredient by its ID
    Then the response status should be 200
    And the response should contain ingredient with name "Pepper"

  Scenario: Ingredient not found
    Given the ingredients database is empty
    When I request ingredient with ID 9999
    Then the response status should be 404