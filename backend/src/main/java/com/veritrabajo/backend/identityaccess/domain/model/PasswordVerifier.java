package com.veritrabajo.backend.identityaccess.domain.model;

/**
 * Domain port (functional interface) for password verification.
 * Allows the AuthUser aggregate to verify passwords without depending
 * on Spring Security's PasswordEncoder directly.
 */
@FunctionalInterface
public interface PasswordVerifier {

    /**
     * Checks whether the raw password matches the encoded hash.
     *
     * @param rawPassword  the plain-text password to verify
     * @param encodedHash  the stored password hash
     * @return true if they match
     */
    boolean matches(String rawPassword, String encodedHash);
}
