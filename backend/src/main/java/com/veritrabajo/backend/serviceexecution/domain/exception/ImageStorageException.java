package com.veritrabajo.backend.serviceexecution.domain.exception;

/**
 * Raised when image storage operations fail due to filesystem constraints.
 */
public class ImageStorageException extends RuntimeException {

    public ImageStorageException(String message) {
        super(message);
    }

    public ImageStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
