package com.vadovates.app.recipevault.recipeingredient;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "recipe_ingredients")
public class RecipeIngredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "recipe_id", nullable = false)
    private Long recipeId;

    @Column(name = "ingredient_id", nullable = false)
    private Long ingredientId;

    @Column (precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(length = 30)
    private String unit;

    @Column (nullable = false)
    private boolean optional = false;

    protected RecipeIngredient() {}

    public RecipeIngredient(Long recipeId, Long ingredientId, BigDecimal amount, String unit, boolean optional) {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.amount = amount;
        this.unit = unit;
        this.optional = optional;
    }

    public Long getId() {
        return id;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }
}
