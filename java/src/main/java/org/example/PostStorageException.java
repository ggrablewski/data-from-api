package org.example;

/**
 * Thrown when a post cannot be written to disk.
 */
public class PostStorageException extends RuntimeException {

    public PostStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
