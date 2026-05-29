package com.gamarraloop.platform.shared.domain.exceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resourceName, Object fieldValue) {
        super(String.format("%s not found with identifier: %s", resourceName, fieldValue));
    }
}
