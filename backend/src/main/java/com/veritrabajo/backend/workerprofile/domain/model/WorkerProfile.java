package com.veritrabajo.backend.workerprofile.domain.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Aggregate root del contexto WorkerProfile.
 * Controla y protege el acceso a las entidades y value objects
 * dentro de este contexto. Nadie puede modificar sus colecciones
 * directamente; toda mutación pasa por sus métodos.
 */
public final class WorkerProfile {

    private final String id;
    private final String fullName;
    private final String phoneNumber;
    private final List<Occupation> occupations;
    private final List<TechnicalSkill> technicalSkills;
    private final List<WorkHistory> workHistories;
    private RawDescription rawDescription;
    private OwnedTools ownedTools;

    private WorkerProfile(String fullName, String phoneNumber) {
        this.id = UUID.randomUUID().toString();
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.ownedTools = OwnedTools.empty();
        this.occupations = new ArrayList<>();
        this.technicalSkills = new ArrayList<>();
        this.workHistories = new ArrayList<>();
    }

    /**
     * Crea un perfil básico con nombre y teléfono.
     * Punto de entrada usado por la ProfileFactory.
     */
    public static WorkerProfile create(String fullName, String phoneNumber) {
        requireNonBlank(fullName, "El nombre completo no puede estar vacío");
        requireNonBlank(phoneNumber, "El teléfono no puede estar vacío");
        return new WorkerProfile(fullName.trim(), phoneNumber.trim());
    }

    /**
     * Asigna la descripción bruta del trabajador.
     * Solo puede asignarse una vez.
     */
    public void assignRawDescription(RawDescription description) {
        if (this.rawDescription != null) {
            throw new IllegalStateException(
                    "La descripción ya fue asignada a este perfil"
            );
        }
        this.rawDescription = description;
    }

    /**
     * Agrega un oficio al perfil. No permite duplicados.
     */
    public void addOccupation(Occupation occupation) {
        if (occupation == null) {
            throw new IllegalArgumentException(
                    "El oficio no puede ser nulo"
            );
        }
        if (occupations.contains(occupation)) {
            throw new IllegalArgumentException(
                    "El oficio ya existe en el perfil: " + occupation.getTradeName()
            );
        }
        occupations.add(occupation);
    }

    /**
     * Agrega una habilidad técnica extraída por la IA.
     * Ignora silenciosamente duplicados.
     */
    public void addTechnicalSkill(TechnicalSkill skill) {
        if (skill == null) {
            throw new IllegalArgumentException(
                    "La habilidad técnica no puede ser nula"
            );
        }
        if (technicalSkills.contains(skill)) {
            return;
        }
        technicalSkills.add(skill);
    }

    /**
     * Agrega un registro de trabajo pasado al historial.
     */
    public void addWorkHistory(WorkHistory history) {
        if (history == null) {
            throw new IllegalArgumentException(
                    "El historial de trabajo no puede ser nulo"
            );
        }
        workHistories.add(history);
    }

    /**
     * Reemplaza las herramientas propias del trabajador.
     */
    public void updateOwnedTools(OwnedTools tools) {
        if (tools == null) {
            throw new IllegalArgumentException(
                    "Las herramientas propias no pueden ser nulas"
            );
        }
        this.ownedTools = tools;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public RawDescription getRawDescription() {
        return rawDescription;
    }

    public OwnedTools getOwnedTools() {
        return ownedTools;
    }

    public List<Occupation> getOccupations() {
        return Collections.unmodifiableList(occupations);
    }

    public List<TechnicalSkill> getTechnicalSkills() {
        return Collections.unmodifiableList(technicalSkills);
    }

    public List<WorkHistory> getWorkHistories() {
        return Collections.unmodifiableList(workHistories);
    }

    public boolean hasOccupations() {
        return !occupations.isEmpty();
    }

    public boolean hasSkills() {
        return !technicalSkills.isEmpty();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof WorkerProfile profile)) {
            return false;
        }
        return id.equals(profile.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "WorkerProfile{id='" + id + "', fullName='" + fullName + "'}";
    }

    private static void requireNonBlank(String value, String errorMessage) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
