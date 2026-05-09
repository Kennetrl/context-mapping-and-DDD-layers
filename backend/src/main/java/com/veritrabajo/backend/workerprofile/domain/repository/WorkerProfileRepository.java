package com.veritrabajo.backend.workerprofile.domain.repository;

import com.veritrabajo.backend.workerprofile.domain.model.AuthUserId;
import com.veritrabajo.backend.workerprofile.domain.model.WorkerId;
import com.veritrabajo.backend.workerprofile.domain.model.WorkerProfile;

import java.util.Optional;

/**
 * Persistence port for {@link WorkerProfile}. Domain depends only on this contract;
 * concrete adapters belong in infrastructure.
 */
public interface WorkerProfileRepository {

    /**
     * Persists (insert or update) a profile aggregate.
     *
     * @param profile aggregate snapshot to store
     * @return persisted aggregate (including generated columns if any)
     */
    WorkerProfile save(WorkerProfile profile);

    /**
     * Loads a profile by aggregate id.
     */
    Optional<WorkerProfile> findById(WorkerId id);

    /**
     * Loads the profile owned by the given authenticated user, if any.
     */
    Optional<WorkerProfile> findByAuthUserId(AuthUserId authUserId);

    /**
     * Idempotency guard: indicates whether a profile already exists for the user.
     */
    boolean existsByAuthUserId(AuthUserId authUserId);

    /**
     * Checks whether a profile already exists for the phone number (duplicate guard).
     *
     * @param phoneNumber normalized phone to check
     * @return {@code true} when taken
     */
    boolean existsByPhoneNumber(String phoneNumber);
}
