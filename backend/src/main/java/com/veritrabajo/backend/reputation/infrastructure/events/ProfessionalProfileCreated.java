package com.veritrabajo.backend.reputation.infrastructure.events;

public record ProfessionalProfileCreated(String profileId) {
    public ProfessionalProfileCreated {
        if (profileId == null || profileId.isBlank()) {
            throw new IllegalArgumentException("Profile id is required");
        }
    }
}
