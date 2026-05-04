package com.veritrabajo.backend.workerprofile.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfaz de Spring Data JPA para operaciones sobre la tabla
 * worker_profiles. Solo vive en infrastructure, el dominio
 * nunca la conoce directamente.
 */
public interface SpringWorkerProfileRepository
        extends JpaRepository<WorkerProfileEntity, String> {

    /**
     * Verifica si existe un perfil con el teléfono dado.
     * Spring Data genera la query automáticamente.
     */
    boolean existsByPhoneNumber(String phoneNumber);
}
