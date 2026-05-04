package com.veritrabajo.backend.workerprofile.domain.model;

import java.util.Collections;
import java.util.List;

/**
 * Objeto que encapsula el resultado del análisis de IA.
 * Transporta los oficios y habilidades extraídos
 * desde la descripción bruta del trabajador.
 */
public final class AnalysisResult {

    private final List<Occupation> occupations;
    private final List<TechnicalSkill> technicalSkills;

    // Constructor privado para forzar el uso del método factory
    private AnalysisResult(
            List<Occupation> occupations,
            List<TechnicalSkill> technicalSkills
    ) {
        this.occupations = Collections.unmodifiableList(occupations);
        this.technicalSkills = Collections.unmodifiableList(technicalSkills);
    }

    /**
     * Crea un resultado de análisis validado.
     * Lanza excepción si alguna lista es nula.
     */
    public static AnalysisResult of(
            List<Occupation> occupations,
            List<TechnicalSkill> technicalSkills
    ) {
        if (occupations == null) {
            throw new IllegalArgumentException(
                    "La lista de oficios no puede ser nula"
            );
        }
        if (technicalSkills == null) {
            throw new IllegalArgumentException(
                    "La lista de habilidades no puede ser nula"
            );
        }
        return new AnalysisResult(occupations, technicalSkills);
    }

    public List<Occupation> getOccupations() {
        return occupations;
    }

    public List<TechnicalSkill> getTechnicalSkills() {
        return technicalSkills;
    }

    public boolean hasResults() {
        return !occupations.isEmpty() || !technicalSkills.isEmpty();
    }

    @Override
    public String toString() {
        return "AnalysisResult{occupations=" + occupations
                + ", technicalSkills=" + technicalSkills + "}";
    }
}
