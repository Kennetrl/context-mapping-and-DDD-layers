package com.veritrabajo.backend.workerprofile.domain.event;

import java.time.Instant;

/**
 * Evento de dominio que se publica cuando un perfil de trabajador
 * ha sido procesado exitosamente por la IA y queda profesionalizado.
 * Otros contextos (ej: Reputation) pueden reaccionar a este evento.
 */
public final class ProfileProfessionalized {

    private final String workerProfileId;
    private final String fullName;
    private final Instant occurredAt;

    // Constructor privado para forzar el uso del método factory
    private ProfileProfessionalized(
            String workerProfileId,
            String fullName
    ) {
        this.workerProfileId = workerProfileId;
        this.fullName = fullName;
        this.occurredAt = Instant.now();
    }

    /**
     * Crea el evento con los datos del perfil recién profesionalizado.
     * Lanza excepción si el ID o nombre son inválidos.
     */
    public static ProfileProfessionalized of(
            String workerProfileId,
            String fullName
    ) {
        if (workerProfileId == null || workerProfileId.isBlank()) {
            throw new IllegalArgumentException(
                    "El ID del perfil no puede estar vacío"
            );
        }
        if (fullName == null || fullName.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del trabajador no puede estar vacío"
            );
        }
        return new ProfileProfessionalized(workerProfileId, fullName);
    }

    public String getWorkerProfileId() {
        return workerProfileId;
    }

    public String getFullName() {
        return fullName;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public String toString() {
        return "ProfileProfessionalized{workerProfileId='" + workerProfileId
                + "', fullName='" + fullName
                + "', occurredAt=" + occurredAt + "}";
    }
}
