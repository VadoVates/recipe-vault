package com.vadovates.app.recipevault.recipe;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (name = "user_id", nullable = false)
    private Long userId;

    @Column (nullable = false, length = 255)
    private String title;

    @Column (columnDefinition = "TEXT")
    private String description;

    @Column (nullable = false, columnDefinition = "TEXT")
    private String instructions;

    @Column
    private Integer servings;

    @Column (name = "prep_time_minutes")
    private Integer prepTimeMinutes;

    @Column (name = "cook_time_minutes")
    private Integer cookTimeMinutes;

    @Column (name = "image_url", length = 500)
    private String imageUrl;

    @Column (name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column (name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Recipe() {}

    public Recipe (Long userId, String title, String description, String instructions, Integer servings,
                   Integer prepTimeMinutes, Integer cookTimeMinutes, String imageUrl) {
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.instructions = instructions;
        this.servings = servings;
        this.prepTimeMinutes = prepTimeMinutes;
        this.cookTimeMinutes = cookTimeMinutes;
        this.imageUrl = imageUrl;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getInstructions() {
        return instructions;
    }

    public Integer getServings() {
        return servings;
    }

    public Integer getPrepTimeMinutes() {
        return prepTimeMinutes;
    }

    public Integer getCookTimeMinutes() {
        return cookTimeMinutes;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public void setServings(Integer servings) {
        this.servings = servings;
    }

    public void setPrepTimeMinutes(Integer prepTimeMinutes) {
        this.prepTimeMinutes = prepTimeMinutes;
    }

    public void setCookTimeMinutes(Integer cookTimeMinutes) {
        this.cookTimeMinutes = cookTimeMinutes;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setUpdatedAt() {
        this.updatedAt = Instant.now();
    }
}
