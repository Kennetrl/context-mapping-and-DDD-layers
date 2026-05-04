package com.veritrabajo.backend.serviceexecution.domain.port;

import com.veritrabajo.backend.serviceexecution.domain.event.ServiceExecutionFinalized;
import com.veritrabajo.backend.serviceexecution.domain.event.ServiceExecutionStarted;
import com.veritrabajo.backend.shared.contract.serviceexecution.ServiceExecutionCompleted;

/**
 * Port (outbound) for publishing events from the ServiceExecution context.
 * Infrastructure adapters implement this interface to bridge events to the
 * messaging/event system (e.g., Spring ApplicationEventPublisher).
 * <p>
 * Includes both internal domain events ({@link ServiceExecutionStarted},
 * {@link ServiceExecutionFinalized}) and the {@link ServiceExecutionCompleted}
 * integration event consumed by Reputation (Partnership relationship).
 */
public interface DomainEventPublisher {

    /**
     * Publishes a {@link ServiceExecutionStarted} domain event.
     *
     * @param event the service order started event to publish
     */
    void publish(ServiceExecutionStarted event);

    /**
     * Publishes a {@link ServiceExecutionFinalized} domain event.
     *
     * @param event the service order finalized event to publish
     */
    void publish(ServiceExecutionFinalized event);

    /**
     * Publishes the {@link ServiceExecutionCompleted} integration event,
     * consumed by the Reputation context (Partnership relationship).
     *
     * @param event the integration event to publish
     */
    void publish(ServiceExecutionCompleted event);
}
