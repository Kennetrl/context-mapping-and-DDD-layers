package com.veritrabajo.backend.identityaccess.domain.model;

import com.veritrabajo.backend.identityaccess.domain.event.UserRegistered;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Aggregate Root for the Identity &amp; Access bounded context.
 * Encapsulates user identity, credential verification, and domain events.
 */
public final class AuthUser {

    private final String id;
    private final String email;
    private final String passwordHash;
    private final Set<Role> roles;

    private AuthUser(
            String id,
            String email,
            String passwordHash
    ) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.roles = new HashSet<>();
    }

    /**
     * Factory method for new user registration.
     * Accepts Value Objects to enforce domain invariants at creation time.
     */
    public static AuthUser register(
            Email email,
            String passwordHash,
            Role role
    ) {
        requireNonBlank(passwordHash, "Password hash cannot be blank");
        if (role == null) {
            throw new IllegalArgumentException("Role is required");
        }
        AuthUser user = new AuthUser(
                UUID.randomUUID().toString(),
                email.value(),
                passwordHash
        );
        user.roles.add(role);
        return user;
    }

    /**
     * Backward-compatible factory for callers using raw strings.
     * Delegates validation to the Email Value Object.
     */
    public static AuthUser register(
            String email,
            String passwordHash,
            Role role
    ) {
        return register(Email.of(email), passwordHash, role);
    }

    public static AuthUser restore(RestoredAuthUser data) {
        requireNonBlank(data.id(), "User id cannot be blank");
        requireNonBlank(data.email(), "Email cannot be blank");
        requireNonBlank(data.passwordHash(), "Password hash cannot be blank");
        Set<Role> roles = data.roles();
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Roles cannot be empty");
        }
        AuthUser user = new AuthUser(
                data.id().trim(),
                data.email().trim().toLowerCase(),
                data.passwordHash()
        );
        user.roles.addAll(roles);
        return user;
    }

    /**
     * Verifies a plain-text password against the stored hash.
     * This domain behavior belongs in the aggregate, not in the application service.
     *
     * @param plainPassword   raw password to verify
     * @param passwordVerifier domain port for password matching (adapter for PasswordEncoder)
     * @return true if the password matches
     */
    public boolean verifyPassword(String plainPassword, PasswordVerifier passwordVerifier) {
        return passwordVerifier.matches(plainPassword, this.passwordHash);
    }

    /**
     * Produces the domain event signaling that registration was completed.
     */
    public UserRegistered registrationCompleted() {
        String primaryRole = roles.iterator().next().name();
        return UserRegistered.of(this.id, this.email, primaryRole);
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    private static void requireNonBlank(
            String value,
            String message
    ) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
    }

    public record RestoredAuthUser(
            String id,
            String email,
            String passwordHash,
            Set<Role> roles
    ) {
    }
}
