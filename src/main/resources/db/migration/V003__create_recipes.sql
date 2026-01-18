CREATE TABLE recipes (
                         id BIGSERIAL PRIMARY KEY,
                         user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                         title VARCHAR(255) NOT NULL,
                         description TEXT,
                         instructions TEXT NOT NULL,
                         servings INT,
                         prep_time_minutes INT,
                         cook_time_minutes INT,
                         image_url VARCHAR(500),
                         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_recipes_user_id ON recipes(user_id);
CREATE INDEX idx_recipes_title ON recipes(title);