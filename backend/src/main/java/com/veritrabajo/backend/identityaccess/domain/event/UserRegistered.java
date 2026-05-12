package com.veritrabajo.backend.identityaccess.domain.event;

import java.time.Instant;
import java.util.Objects;

/**
 * Domain event raised when a new user successfully registers.
 * Other bounded contexts (Customer, WorkerProfile) can react to this event.
 */
public final class UserRegistered {

    private final String userId;
    private final String email;
    private final String role;
    private final Instant occurredAt;

    private UserRegistered(String userId, String email, String role, Instant occurredAt) {
        this.userId = Objects.requireNonNull(userId, "userId cannot be null");
        this.email = Objects.requireNonNull(email, "email cannot be null");
        this.role = Objects.requireNonNull(role, "role cannot be null");
        this.occurredAt = Objects.requireNonNull(occurredAt, "occurredAt cannot be null");
    }

    public static UserRegistered of(String userId, String email, String role) {
        return new UserRegistered(userId, email, role, Instant.now());
    }

    public String getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }

    @Override
    public String toString() {
        return "UserRegistered{userId='" + userId
                + "', email='" + email
                + "', role='" + role
                + "', occurredAt=" + occurredAt + "}";
    }
}
