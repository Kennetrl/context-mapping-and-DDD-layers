package com.veritrabajo.backend.workerprofile.domain.model;

import java.time.LocalDate;
import java.util.UUID;

/**
 * Entidad que representa un trabajo pasado del trabajador.
 * Tiene identidad propia (ID) pero solo puede existir
 * dentro del agregado WorkerProfile.
 */
public final class WorkHistory {

    private final String id;
    private final String clientOrCompany;
    private final String description;
    private final LocalDate startDate;
    private final LocalDate endDate;

    private WorkHistory(String id, JobData jobData, DateRange range) {
        this.id = id;
        this.clientOrCompany = jobData.clientOrCompany();
        this.description = jobData.description();
        this.startDate = range.start();
        this.endDate = range.end();
    }

    /**
     * Crea un registro de trabajo pasado validado.
     * Lanza excepción si los campos obligatorios son inválidos.
     */
    public static WorkHistory of(
            String clientOrCompany,
            String description,
            LocalDate startDate
    ) {
        validateClientOrCompany(clientOrCompany);
        validateDescription(description);
        validateStartDate(startDate);
        JobData jobData = new JobData(clientOrCompany.trim(), description.trim());
        DateRange range = new DateRange(startDate, null);
        return new WorkHistory(UUID.randomUUID().toString(), jobData, range);
    }

    /**
     * Devuelve una nueva instancia con la fecha de fin establecida.
     */
    public WorkHistory withEndDate(LocalDate endDate) {
        if (endDate != null && endDate.isBefore(this.startDate)) {
            throw new IllegalArgumentException(
                    "La fecha de fin no puede ser anterior a la de inicio"
            );
        }
        JobData jobData = new JobData(this.clientOrCompany, this.description);
        DateRange range = new DateRange(this.startDate, endDate);
        return new WorkHistory(this.id, jobData, range);
    }

    public String getId() {
        return id;
    }

    public String getClientOrCompany() {
        return clientOrCompany;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public boolean isCurrentJob() {
        return endDate == null;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof WorkHistory that)) {
            return false;
        }
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "WorkHistory{id='" + id
                + "', clientOrCompany='" + clientOrCompany + "'}";
    }

    private static void validateClientOrCompany(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "El cliente o empresa no puede estar vacío"
            );
        }
    }

    private static void validateDescription(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(
                    "La descripción del trabajo no puede estar vacía"
            );
        }
    }

    private static void validateStartDate(LocalDate value) {
        if (value == null) {
            throw new IllegalArgumentException(
                    "La fecha de inicio no puede ser nula"
            );
        }
    }

    private record JobData(String clientOrCompany, String description) { }

    private record DateRange(LocalDate start, LocalDate end) { }
}
