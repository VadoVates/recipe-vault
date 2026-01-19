package com.vadovates.app.recipevault.user;

import java.time.Instant;

public record UserDto(
        Long id,
        String email,
        String displayName,
        Instant createdAt,
        Instant updatedAt
) {
    public static UserDto from(User user) {
    return new UserDto(
            user.getId(),
            user.getEmail(),
            user.getDisplayName(),
            user.getCreatedAt(),
            user.getUpdatedAt()
    );
    }
}
