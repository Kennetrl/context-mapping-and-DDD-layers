package com.veritrabajo.backend.serviceorder.domain.port;

import com.veritrabajo.backend.serviceorder.domain.event.ServiceOrderFinalized;
import com.veritrabajo.backend.serviceorder.domain.event.ServiceOrderStarted;

/**
 * Port (outbound) for publishing domain events from the ServiceOrder context.
 * Infrastructure adapters implement this interface to bridge domain events
 * to the actual messaging/event system (e.g., Spring ApplicationEventPublisher).
 */
public interface DomainEventPublisher {

    /**
     * Publishes a {@link ServiceOrderStarted} domain event.
     *
     * @param event the service order started event to publish
     */
    void publish(ServiceOrderStarted event);

    /**
     * Publishes a {@link ServiceOrderFinalized} domain event.
     *
     * @param event the service order finalized event to publish
     */
    void publish(ServiceOrderFinalized event);
}
