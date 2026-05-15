package com.veritrabajo.backend.identityaccess.infrastructure.persistence;

import com.veritrabajo.backend.identityaccess.domain.model.AuthUser;

import java.util.HashSet;

/**
 * Mapper responsible for bidirectional conversion between the AuthUser
 * aggregate (domain) and AuthUserEntity (infrastructure/persistence).
 * Keeps mapping logic out of the repository implementation.
 */
public final class AuthUserMapper {

    private AuthUserMapper() {
        // utility class
    }

    /**
     * Converts a persistence entity to the domain aggregate.
     */
    public static AuthUser toDomain(AuthUserEntity entity) {
        return AuthUser.restore(new AuthUser.RestoredAuthUser(
                entity.getId(),
                entity.getEmail(),
                entity.getPasswordHash(),
                entity.getRoles()
        ));
    }

    /**
     * Updates a persistence entity from the domain aggregate.
     * Used for both create and update operations.
     */
    public static void updateEntity(AuthUserEntity target, AuthUser source) {
        target.setId(source.getId());
        target.setEmail(source.getEmail());
        target.setPasswordHash(source.getPasswordHash());
        target.setRoles(new HashSet<>(source.getRoles()));
    }
}
