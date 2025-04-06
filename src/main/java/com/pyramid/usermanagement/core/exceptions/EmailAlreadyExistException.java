package com.pyramid.usermanagement.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Suvorov Vassilievitch
 * Date: 06/04/2025
 * Time: 00:30
 * Project Name: user-management
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class EmailAlreadyExistException extends RuntimeException {
    public EmailAlreadyExistException(String message) {
        super(message);
    }
}
