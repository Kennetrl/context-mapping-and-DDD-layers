package com.veritrabajo.backend.serviceexecution.application;

/**
 * Request body for completing a service execution.
 * Carries the client's rating and comment, required to publish the
 * {@code ServiceExecutionCompleted} integration event consumed by Reputation.
 */
public record CompleteServiceExecutionRequest(int clientRating, String clientComment) {
}
