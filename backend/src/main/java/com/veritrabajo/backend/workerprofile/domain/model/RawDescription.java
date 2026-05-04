package com.veritrabajo.backend.workerprofile.domain.model;

/**
 * Value Object que representa el texto libre ingresado por el trabajador
 * para describir su experiencia laboral. Es inmutable y no puede estar vacío.
 */
public final class RawDescription {

    private final String text;

    private RawDescription(String text) {
        this.text = text;
    }

    /**
     * Crea una instancia validada de RawDescription.
     * Lanza excepción si el texto es nulo o vacío.
     */
    public static RawDescription of(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(
                    "La descripción no puede estar vacía"
            );
        }
        return new RawDescription(text.trim());
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof RawDescription that)) {
            return false;
        }
        return text.equals(that.text);
    }

    @Override
    public int hashCode() {
        return text.hashCode();
    }

    @Override
    public String toString() {
        return "RawDescription{text='" + text + "'}";
    }
}
