package com.veritrabajo.backend.identityaccess.domain.port;

import com.veritrabajo.backend.identityaccess.domain.model.AuthUser;

import java.util.Optional;

/**
 * Repository port for AuthUser aggregate persistence.
 * Defined in domain/port for consistency with other bounded contexts
 * (Customer, Reputation, JobMarketplace all use domain/port).
 */
public interface AuthUserRepository {

    AuthUser save(AuthUser user);

    Optional<AuthUser> findByEmail(String email);

    Optional<AuthUser> findById(String id);

    boolean existsByEmail(String email);
}
