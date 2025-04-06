package com.pyramid.usermanagement.domain.security.model.dto;

import org.springframework.boot.context.properties.bind.DefaultValue;

import java.util.Set;

public record JwtResponse(
        String token,
        @DefaultValue("Bearer")
        String type,
        Long id,
        String username,
        String email,
        Set<String> roles

) {
}
