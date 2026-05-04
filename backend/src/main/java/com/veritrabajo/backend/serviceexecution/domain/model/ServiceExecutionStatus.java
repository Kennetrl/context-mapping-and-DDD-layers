package com.veritrabajo.backend.serviceexecution.domain.model;

/**
 * Lifecycle states for a ServiceExecution aggregate.
 */
public enum ServiceExecutionStatus {
    STARTED,
    IN_PROCESS,
    FINALIZED,
    DISPUTED
}
