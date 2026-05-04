package com.veritrabajo.backend.serviceexecution.infrastructure.events;

import com.veritrabajo.backend.serviceexecution.domain.event.ServiceExecutionFinalized;
import com.veritrabajo.backend.serviceexecution.domain.event.ServiceExecutionStarted;
import com.veritrabajo.backend.serviceexecution.domain.port.DomainEventPublisher;
import com.veritrabajo.backend.shared.contract.serviceexecution.ServiceExecutionCompleted;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Infrastructure adapter that bridges ServiceExecution events to Spring's
 * {@link ApplicationEventPublisher}.
 * <p>
 * Enables the Reputation bounded context to subscribe to ServiceExecution events
 * via standard Spring {@code @EventListener} mechanisms, fulfilling the
 * ServiceExecution (U) ↔ Reputation (D) Partnership relationship.
 */
@Component("serviceExecutionDomainEventPublisher")
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringDomainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publish(ServiceExecutionStarted event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publish(ServiceExecutionFinalized event) {
        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void publish(ServiceExecutionCompleted event) {
        applicationEventPublisher.publishEvent(event);
    }
}
