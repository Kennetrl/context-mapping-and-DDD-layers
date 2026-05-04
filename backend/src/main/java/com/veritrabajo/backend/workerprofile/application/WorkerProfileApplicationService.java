package com.veritrabajo.backend.workerprofile.application;

import com.veritrabajo.backend.workerprofile.application.dto.RegisterWorkerRequest;
import com.veritrabajo.backend.workerprofile.application.dto.RegisterWorkerResponse;
import com.veritrabajo.backend.workerprofile.domain.factory.ProfileFactory;
import com.veritrabajo.backend.workerprofile.domain.model.WorkerProfile;
import com.veritrabajo.backend.workerprofile.domain.repository.WorkerProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio de aplicación que orquesta el caso de uso de registro.
 * No contiene lógica de dominio, solo coordina:
 * validaciones de entrada → factory → repositorio → respuesta.
 */
@Service
public class WorkerProfileApplicationService {

    private final ProfileFactory profileFactory;
    private final WorkerProfileRepository profileRepository;

    public WorkerProfileApplicationService(
            ProfileFactory profileFactory,
            WorkerProfileRepository profileRepository
    ) {
        this.profileFactory = profileFactory;
        this.profileRepository = profileRepository;
    }

    /**
     * Registra un nuevo trabajador en el sistema.
     * Valida que no exista duplicado, crea el perfil con IA
     * y lo persiste en la base de datos.
     *
     * @param request datos del trabajador desde el frontend
     * @return respuesta con mensaje de éxito e ID del perfil
     */
    @Transactional
    public RegisterWorkerResponse registerWorker(
            RegisterWorkerRequest request
    ) {
        // Paso 1: Verificar que no haya duplicado por teléfono
        validateNoDuplicate(request.getPhoneNumber());

        // Paso 2: Usar la factory para crear el perfil completo con IA
        WorkerProfile profile = profileFactory.createFromDescription(
                request.getFullName(),
                request.getPhoneNumber(),
                request.getExperienceDescription()
        );

        // Paso 3: Persistir el perfil en la base de datos
        WorkerProfile saved = profileRepository.save(profile);

        // Paso 4: Devolver respuesta simple al controlador
        return RegisterWorkerResponse.success(saved.getId());
    }

    /**
     * Verifica que no exista otro trabajador con el mismo teléfono.
     * Lanza excepción si ya está registrado.
     */
    private void validateNoDuplicate(String phoneNumber) {
        if (profileRepository.existsByPhoneNumber(phoneNumber)) {
            throw new IllegalStateException(
                    "Ya existe un perfil registrado con ese número de teléfono"
            );
        }
    }
}
