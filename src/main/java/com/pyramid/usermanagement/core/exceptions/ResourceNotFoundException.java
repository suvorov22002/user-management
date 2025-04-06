package com.pyramid.usermanagement.core.exceptions;

/**
 * Created by Suvorov Vassilievitch
 * Date: 06/04/2025
 * Time: 00:59
 * Project Name: user-management
 */
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
