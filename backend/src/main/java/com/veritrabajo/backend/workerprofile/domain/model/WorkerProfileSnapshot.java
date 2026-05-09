package com.veritrabajo.backend.workerprofile.domain.model;

/**
 * Data carrier used for rehydrating a {@link WorkerProfile} aggregate from persistence.
 * Mirrors the {@code CustomerData} pattern in the Customer context.
 */
public record WorkerProfileSnapshot(
        WorkerId id,
        AuthUserId authUserId,
        String fullName,
        String phoneNumber
) { }
