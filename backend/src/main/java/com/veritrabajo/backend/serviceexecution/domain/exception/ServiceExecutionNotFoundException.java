package com.veritrabajo.backend.serviceexecution.domain.exception;

import java.util.UUID;

/**
 * Raised when a service execution does not exist in the repository.
 */
public class ServiceExecutionNotFoundException extends RuntimeException {

    public ServiceExecutionNotFoundException(UUID executionId) {
        super("Execution not found: " + executionId);
    }
}
