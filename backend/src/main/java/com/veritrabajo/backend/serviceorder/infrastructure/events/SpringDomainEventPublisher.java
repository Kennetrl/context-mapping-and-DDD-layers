package com.veritrabajo.backend.serviceorder.infrastructure.events;

import com.veritrabajo.backend.serviceorder.domain.event.ServiceOrderFinalized;
import com.veritrabajo.backend.serviceorder.domain.event.ServiceOrderStarted;
import com.veritrabajo.backend.serviceorder.domain.port.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Infrastructure adapter that bridges domain events to Spring's
 * {@link ApplicationEventPublisher}.
 * <p>
 * This enables the Reputation bounded context to subscribe to ServiceOrder
 * domain events via standard Spring {@code @EventListener} mechanisms,
 * fulfilling the ServiceExecution (U) ↔ Reputation (D) Partnership relationship
 * defined in the context map.
 */
@Component("serviceOrderDomainEventPublisher")
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringDomainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(ServiceOrderStarted event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publish(ServiceOrderFinalized event) {
        applicationEventPublisher.publishEvent(event);
    }
}
