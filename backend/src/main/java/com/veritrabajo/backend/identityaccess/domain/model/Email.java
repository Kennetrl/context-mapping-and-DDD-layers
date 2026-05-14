package com.veritrabajo.backend.identityaccess.domain.model;

/**
 * Value Object representing a validated, normalized email address.
 * Encapsulates email validation and normalization rules that belong
 * in the domain rather than in application services.
 */
public record Email(String value) {

    public Email {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }
        value = value.trim().toLowerCase();
    }

    public static Email of(String raw) {
        return new Email(raw);
    }

    @Override
    public String toString() {
        return value;
    }
}
