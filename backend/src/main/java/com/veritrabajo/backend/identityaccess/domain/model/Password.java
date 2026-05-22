package com.veritrabajo.backend.identityaccess.domain.model;

/**
 * Value Object representing a raw password before hashing.
 * Encapsulates password presence validation that belongs in the domain.
 */
public record Password(String value) {

    public Password {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Password is required");
        }
    }

    public static Password of(String raw) {
        return new Password(raw);
    }

    @Override
    public String toString() {
        return "***";
    }
}
