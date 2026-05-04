package com.veritrabajo.backend.serviceexecution.application;

/**
 * Request body for creating a new service execution.
 */
public record CreateServiceExecutionRequest(String clientId, String workerId) {
}
