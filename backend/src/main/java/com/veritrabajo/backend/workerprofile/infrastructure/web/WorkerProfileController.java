package com.veritrabajo.backend.workerprofile.infrastructure.web;

import com.veritrabajo.backend.workerprofile.application.WorkerProfileApplicationService;
import com.veritrabajo.backend.workerprofile.application.dto.RegisterWorkerRequest;
import com.veritrabajo.backend.workerprofile.application.dto.RegisterWorkerResponse;
import com.veritrabajo.backend.workerprofile.domain.model.AuthUserId;
import com.veritrabajo.backend.workerprofile.domain.port.AuthenticatedIdentityProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST adapter for worker registration.
 *
 * <p>The current authenticated user is resolved through the
 * {@link AuthenticatedIdentityProvider} domain port — the controller deliberately does
 * <strong>not</strong> import Spring Security types nor use {@code @AuthenticationPrincipal},
 * keeping the cross-context dependency confined to the adapter in
 * {@code workerprofile/infrastructure/acl/}.
 */
@RestController
@RequestMapping("/api/profiles")
public class WorkerProfileController {

    private final WorkerProfileApplicationService applicationService;
    private final AuthenticatedIdentityProvider identityProvider;

    public WorkerProfileController(
            WorkerProfileApplicationService applicationService,
            AuthenticatedIdentityProvider identityProvider
    ) {
        this.applicationService = applicationService;
        this.identityProvider = identityProvider;
    }

    /**
     * Registers a worker profile for the currently authenticated user.
     *
     * <p>{@code 201 Created} on success; {@code 400} for invalid input; {@code 409} for
     * conflicts (duplicate phone or profile already exists for the user); {@code 401/403}
     * when there is no authenticated principal. Mappings handled by
     * {@link com.veritrabajo.backend.shared.api.ApiExceptionHandler}.
     */
    @PostMapping
    public ResponseEntity<RegisterWorkerResponse> registerWorker(
            @RequestBody RegisterWorkerRequest request
    ) {
        AuthUserId authUserId = identityProvider.currentAuthUserId();
        RegisterWorkerResponse response = applicationService.registerWorker(authUserId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
