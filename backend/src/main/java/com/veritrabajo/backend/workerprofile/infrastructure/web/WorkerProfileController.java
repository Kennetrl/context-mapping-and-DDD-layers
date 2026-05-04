package com.veritrabajo.backend.workerprofile.infrastructure.web;

import com.veritrabajo.backend.workerprofile.application.WorkerProfileApplicationService;
import com.veritrabajo.backend.workerprofile.application.dto.RegisterWorkerRequest;
import com.veritrabajo.backend.workerprofile.application.dto.RegisterWorkerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST que expone el endpoint de registro de trabajadores.
 * Solo recibe la petición HTTP, delega al ApplicationService
 * y devuelve la respuesta. No contiene lógica de negocio.
 */
@RestController
@RequestMapping("/api/profiles")
public class WorkerProfileController {

    private final WorkerProfileApplicationService applicationService;

    public WorkerProfileController(
            WorkerProfileApplicationService applicationService
    ) {
        this.applicationService = applicationService;
    }

    /**
     * Registra un nuevo trabajador en el sistema.
     * Recibe los datos del formulario de registro del frontend.
     * Devuelve 201 CREATED con mensaje de éxito si todo va bien.
     * Devuelve 400 BAD REQUEST si los datos son inválidos o duplicados.
     *
     * POST /api/profiles
     */
    @PostMapping
    public ResponseEntity<RegisterWorkerResponse> registerWorker(
            @RequestBody RegisterWorkerRequest request
    ) {
        try {
            RegisterWorkerResponse response =
                    applicationService.registerWorker(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }
}
