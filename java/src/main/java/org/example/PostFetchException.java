package org.example;

/**
 * Thrown when posts cannot be retrieved from the remote API.
 */
public class PostFetchException extends RuntimeException {

    public PostFetchException(String message, Throwable cause) {
        super(message, cause);
    }

    public PostFetchException(String message) {
        super(message);
    }
}
