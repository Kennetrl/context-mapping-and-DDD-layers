package com.veritrabajo.backend.customer.application.dto;

import java.util.UUID;

public record RequestServiceRequest(UUID jobPostId, UUID addressId) { }
