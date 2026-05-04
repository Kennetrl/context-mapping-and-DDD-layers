package com.veritrabajo.backend.workerprofile.domain.model;

/**
 * Value Object que representa un oficio del trabajador
 * junto con su nivel de expertise. Ej: ("Plomero", "Avanzado").
 * Un trabajador puede tener múltiples oficios.
 */
public final class Occupation {

    // Niveles de expertise permitidos en el sistema
    public enum ExpertiseLevel {
        BEGINNER, INTERMEDIATE, ADVANCED, EXPERT
    }

    private final String tradeName;
    private final ExpertiseLevel level;

    // Constructor privado para forzar el uso del método factory
    private Occupation(String tradeName, ExpertiseLevel level) {
        this.tradeName = tradeName;
        this.level = level;
    }

    /**
     * Crea un oficio validado con su nivel de expertise.
     * Lanza excepción si el nombre es nulo o vacío.
     */
    public static Occupation of(String tradeName, ExpertiseLevel level) {
        if (tradeName == null || tradeName.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre del oficio no puede estar vacío"
            );
        }
        if (level == null) {
            throw new IllegalArgumentException(
                    "El nivel de expertise no puede ser nulo"
            );
        }
        return new Occupation(tradeName.trim(), level);
    }

    public String getTradeName() {
        return tradeName;
    }

    public ExpertiseLevel getLevel() {
        return level;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Occupation that)) {
            return false;
        }
        return tradeName.equals(that.tradeName) && level == that.level;
    }

    @Override
    public int hashCode() {
        return 31 * tradeName.hashCode() + level.hashCode();
    }

    @Override
    public String toString() {
        return "Occupation{tradeName='" + tradeName + "', level=" + level + "}";
    }
}
