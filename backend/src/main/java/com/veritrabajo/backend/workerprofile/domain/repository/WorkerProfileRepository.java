package com.veritrabajo.backend.workerprofile.domain.repository;

import com.veritrabajo.backend.workerprofile.domain.model.WorkerProfile;

/**
 * Interfaz del repositorio definida en el dominio.
 * El dominio solo conoce este contrato, nunca los detalles
 * de JPA o la base de datos. La implementación real
 * vive en infrastructure/persistence/.
 */
public interface WorkerProfileRepository {

    /**
     * Guarda un perfil de trabajador en la base de datos.
     *
     * @param profile el perfil completo a persistir
     * @return el perfil guardado con cualquier dato generado por la BD
     */
    WorkerProfile save(WorkerProfile profile);

    /**
     * Busca un perfil por su identificador único.
     *
     * @param id el identificador del perfil
     * @return el perfil encontrado o null si no existe
     */
    WorkerProfile findById(String id);

    /**
     * Verifica si ya existe un perfil con ese número de teléfono.
     * Evita registros duplicados en el sistema.
     *
     * @param phoneNumber el teléfono a verificar
     * @return true si ya existe un perfil con ese teléfono
     */
    boolean existsByPhoneNumber(String phoneNumber);
}
