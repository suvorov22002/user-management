package com.pyramid.usermanagement.domain.security.jwt;

import com.pyramid.usermanagement.domain.security.model.SecurityUser;
import com.pyramid.usermanagement.domain.security.repository.SecurityUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by Suvorov Vassilievitch
 * Date: 06/04/2025
 * Time: 21:47
 * Project Name: user-management
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final SecurityUserRepository userRepository;

    public UserDetailsServiceImpl(SecurityUserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SecurityUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(user);
    }
}
