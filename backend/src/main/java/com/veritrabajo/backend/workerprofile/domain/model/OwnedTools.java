package com.veritrabajo.backend.workerprofile.domain.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Value Object que representa el conjunto de herramientas
 * que el trabajador posee. Es inmutable: cualquier modificación
 * devuelve una nueva instancia.
 */
public final class OwnedTools {

    private final Set<String> tools;

    private OwnedTools(Set<String> tools) {
        this.tools = Collections.unmodifiableSet(new HashSet<>(tools));
    }

    /**
     * Crea una lista de herramientas vacía.
     */
    public static OwnedTools empty() {
        return new OwnedTools(new HashSet<>());
    }

    /**
     * Crea una lista de herramientas desde un conjunto existente.
     * Filtra automáticamente valores nulos o vacíos.
     */
    public static OwnedTools of(Set<String> tools) {
        if (tools == null) {
            return empty();
        }
        Set<String> cleanTools = new HashSet<>();
        for (String tool : tools) {
            if (tool != null && !tool.isBlank()) {
                cleanTools.add(tool.trim());
            }
        }
        return new OwnedTools(cleanTools);
    }

    /**
     * Devuelve una nueva instancia con la herramienta agregada.
     */
    public OwnedTools addTool(String toolName) {
        if (toolName == null || toolName.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la herramienta no puede estar vacío"
            );
        }
        Set<String> updated = new HashSet<>(tools);
        updated.add(toolName.trim());
        return new OwnedTools(updated);
    }

    public Set<String> getTools() {
        return tools;
    }

    public boolean isEmpty() {
        return tools.isEmpty();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof OwnedTools that)) {
            return false;
        }
        return tools.equals(that.tools);
    }

    @Override
    public int hashCode() {
        return tools.hashCode();
    }

    @Override
    public String toString() {
        return "OwnedTools{tools=" + tools + "}";
    }
}
