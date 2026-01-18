CREATE TABLE recipe_ingredients (
                                    id BIGSERIAL PRIMARY KEY,
                                    recipe_id BIGINT NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,
                                    ingredient_id BIGINT NOT NULL REFERENCES ingredients(id) ON DELETE RESTRICT,
                                    amount DECIMAL(10, 2),
                                    unit VARCHAR(30),
                                    optional BOOLEAN NOT NULL DEFAULT FALSE,
                                    UNIQUE(recipe_id, ingredient_id)
);

CREATE INDEX idx_recipe_ingredients_recipe ON recipe_ingredients(recipe_id);
CREATE INDEX idx_recipe_ingredients_ingredient ON recipe_ingredients(ingredient_id);