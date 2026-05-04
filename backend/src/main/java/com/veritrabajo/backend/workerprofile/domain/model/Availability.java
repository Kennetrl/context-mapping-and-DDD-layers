package com.veritrabajo.backend.workerprofile.domain.model;

/**
 * Value Object — Availability
 *
 * Represents the geographic zone or city where the worker
 * is available to take jobs.
 *
 * This is used by the Marketplace module to match workers
 * with nearby job requests.
 *
 * Examples: "North Quito", "Guayaquil - Centro", "Cuenca"
 *
 * Invariants:
 *  - City or zone cannot be null or blank.
 *  - Text is trimmed automatically on creation.
 */
public record Availability(String cityOrZone) {

    public Availability {
        if (cityOrZone == null || cityOrZone.isBlank()) {
            throw new IllegalArgumentException(
                    "City or zone cannot be empty."
            );
        }
        cityOrZone = cityOrZone.trim();
    }
}
