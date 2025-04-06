package com.pyramid.usermanagement.domain.security.controler;

import com.pyramid.usermanagement.domain.security.jwt.JwtUtils;
import com.pyramid.usermanagement.domain.security.jwt.UserDetailsImpl;
import com.pyramid.usermanagement.domain.security.model.ERole;
import com.pyramid.usermanagement.domain.security.model.Role;
import com.pyramid.usermanagement.domain.security.model.SecurityUser;
import com.pyramid.usermanagement.domain.security.model.dto.JwtResponse;
import com.pyramid.usermanagement.domain.security.model.dto.LoginRequest;
import com.pyramid.usermanagement.domain.security.model.dto.MessageResponse;
import com.pyramid.usermanagement.domain.security.model.dto.SignupRequest;
import com.pyramid.usermanagement.domain.security.repository.RoleRepository;
import com.pyramid.usermanagement.domain.security.repository.SecurityUserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Suvorov Vassilievitch
 * Date: 06/04/2025
 * Time: 22:51
 * Project Name: user-management
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final SecurityUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails  = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return ResponseEntity.ok(new JwtResponse(jwt, "Bearer",
                userDetails.getId(), userDetails.getUsername(),
                userDetails.getEmail(), new HashSet<>(roles)));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signupRequest) {

        if (userRepository.existsByUsername(signupRequest.username())) {
            return ResponseEntity
                    .badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signupRequest.email())) {
            return ResponseEntity
                    .badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        SecurityUser user = new SecurityUser();
        user.setUsername(signupRequest.username());
        user.setEmail(signupRequest.email());
        user.setPassword(encoder.encode(signupRequest.password()));

        Set<String> strRoles = signupRequest.roles();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "ROLE_ADMIN":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found"));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
