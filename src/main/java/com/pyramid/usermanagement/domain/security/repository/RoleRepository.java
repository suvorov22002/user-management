package com.pyramid.usermanagement.domain.security.repository;

import com.pyramid.usermanagement.domain.security.model.ERole;
import com.pyramid.usermanagement.domain.security.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
