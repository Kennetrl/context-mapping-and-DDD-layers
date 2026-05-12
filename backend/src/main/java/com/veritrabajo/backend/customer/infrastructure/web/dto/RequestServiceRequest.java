package com.veritrabajo.backend.customer.infrastructure.web.dto;

import java.util.UUID;

public record RequestServiceRequest(UUID jobPostId, UUID addressId) { }
