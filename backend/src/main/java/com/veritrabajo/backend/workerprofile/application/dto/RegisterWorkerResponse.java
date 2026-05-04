package com.veritrabajo.backend.workerprofile.application.dto;

/**
 * DTO de respuesta que el endpoint devuelve al frontend
 * cuando el registro del trabajador fue exitoso.
 * Contiene solo el mensaje de éxito y el ID generado.
 */
public class RegisterWorkerResponse {

    private final String message;
    private final String profileId;

    // Constructor privado para forzar el uso del método factory
    private RegisterWorkerResponse(String message, String profileId) {
        this.message = message;
        this.profileId = profileId;
    }

    /**
     * Crea una respuesta de registro exitoso.
     *
     * @param profileId el ID del perfil recién creado
     * @return respuesta con mensaje de éxito
     */
    public static RegisterWorkerResponse success(String profileId) {
        return new RegisterWorkerResponse(
                "Perfil registrado exitosamente",
                profileId
        );
    }

    public String getMessage() {
        return message;
    }

    public String getProfileId() {
        return profileId;
    }
}
