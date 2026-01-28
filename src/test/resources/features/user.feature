Feature: User Management
  As a client application
  I want to manage user accounts
  So that users can access RecipeVault

  Scenario: Create a new user
    Given the users database is empty
    When I create the user with email "test@test.com" and password "hash" and display name "User1"
    Then the response status should be 201
    And the response should contain user with name "User1"