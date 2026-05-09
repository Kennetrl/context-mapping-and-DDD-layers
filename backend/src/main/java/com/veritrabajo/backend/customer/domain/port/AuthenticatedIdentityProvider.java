package com.veritrabajo.backend.customer.domain.port;

import com.veritrabajo.backend.customer.domain.model.AuthUserId;

/**
 * Domain port that exposes the currently authenticated user as a local value object.
 *
 * <p>Concrete adapters live in {@code customer/infrastructure/acl/} and are the only
 * place in this module allowed to depend on Spring Security or the IdentityAccess context.
 * The application layer depends solely on this interface so the domain remains free of
 * cross-context types.
 */
public interface AuthenticatedIdentityProvider {

    /**
     * @return the {@link AuthUserId} of the principal currently authenticated on this thread
     * @throws com.veritrabajo.backend.customer.domain.exception
     *         .AuthenticationRequiredException when no authenticated principal is available
     */
    AuthUserId currentAuthUserId();
}
