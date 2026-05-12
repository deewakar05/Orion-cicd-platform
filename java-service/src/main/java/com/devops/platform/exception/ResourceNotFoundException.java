package com.devops.platform.exception;

/**
 * ResourceNotFoundException
 * Thrown when a requested resource (e.g., report by ID) does not exist.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceType, String id) {
        super(String.format("%s not found with id: %s", resourceType, id));
    }
}
