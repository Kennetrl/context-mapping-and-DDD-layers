package com.veritrabajo.backend.identityaccess.domain.model;

/**
 * Value Object representing the role of an authenticated user.
 * Parsing logic is encapsulated here rather than in application services.
 */
public enum Role {
    CUSTOMER,
    WORKER;

    /**
     * Parses a string into a Role, applying trim and uppercase normalization.
     *
     * @param value the raw role string
     * @return the matching Role
     * @throws IllegalArgumentException if value is blank or not a valid role
     */
    public static Role from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Role is required");
        }
        try {
            return Role.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Role must be CUSTOMER or WORKER");
        }
    }
}
