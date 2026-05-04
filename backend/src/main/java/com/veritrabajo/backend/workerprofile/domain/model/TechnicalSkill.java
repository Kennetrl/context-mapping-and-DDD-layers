package com.veritrabajo.backend.workerprofile.domain.model;

/**
 * Value Object que representa una habilidad técnica específica
 * extraída automáticamente por la IA desde la descripción del trabajador.
 * Ej: "Soldadura de cobre", "Instalación de drywall".
 */
public final class TechnicalSkill {

    private final String skillName;

    private TechnicalSkill(String skillName) {
        this.skillName = skillName;
    }

    /**
     * Crea una habilidad técnica validada.
     * Lanza excepción si el nombre es nulo o vacío.
     */
    public static TechnicalSkill of(String skillName) {
        if (skillName == null || skillName.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la habilidad no puede estar vacío"
            );
        }
        return new TechnicalSkill(skillName.trim());
    }

    public String getSkillName() {
        return skillName;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TechnicalSkill that)) {
            return false;
        }
        return skillName.equalsIgnoreCase(that.skillName);
    }

    @Override
    public int hashCode() {
        return skillName.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return "TechnicalSkill{skillName='" + skillName + "'}";
    }
}
