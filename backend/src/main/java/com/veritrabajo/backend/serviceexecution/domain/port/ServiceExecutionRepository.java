package com.veritrabajo.backend.serviceexecution.domain.port;

import com.veritrabajo.backend.serviceexecution.domain.model.ServiceExecution;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository port for the ServiceExecution aggregate root.
 */
public interface ServiceExecutionRepository {

    Optional<ServiceExecution> findById(UUID id);

    ServiceExecution save(ServiceExecution execution);

    List<ServiceExecution> findByWorkerId(String workerId);
}
