package com.pyramid.usermanagement.domain.user.services.implement;

import com.pyramid.usermanagement.core.exceptions.EmailAlreadyExistException;
import com.pyramid.usermanagement.core.exceptions.ResourceNotFoundException;
import com.pyramid.usermanagement.domain.user.dto.UserCreateDTO;
import com.pyramid.usermanagement.domain.user.dto.UserDTO;
import com.pyramid.usermanagement.domain.user.dto.UserUpdateDTO;
import com.pyramid.usermanagement.domain.user.model.AppUser;
import com.pyramid.usermanagement.domain.user.repository.UserRepository;
import com.pyramid.usermanagement.domain.user.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Suvorov Vassilievitch
 * Date: 06/04/2025
 * Time: 00:14
 * Project Name: user-management
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private UserDTO convertEntityToDTO(AppUser user) {
        return new UserDTO(user.getId(), user.getLogin(), user.getName(), user.getEmail(), user.getCreatedDate());
    }
    @Override
    @Transactional
    public UserDTO createUser(UserCreateDTO userCreateDTO) {

        if (userRepository.existsByEmail(userCreateDTO.email())) {
            throw new EmailAlreadyExistException("Email already exists: " + userCreateDTO.email());
        }

        AppUser user = new AppUser();
        user.setCreatedDate(LocalDateTime.now());
        user.setLogin(userCreateDTO.login());
        user.setName(userCreateDTO.name());
        user.setEmail(userCreateDTO.email());

        AppUser savedUser = userRepository.save(user);
        return convertEntityToDTO(savedUser);

    }

    @Override
    public List<UserDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());

    }

    @Override
    public Page<UserDTO> getAllUsersPaginated(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::convertEntityToDTO);
    }

    @Override
    public UserDTO getUserById(Long id) {
        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convertEntityToDTO(user);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        AppUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return convertEntityToDTO(user);
    }

    @Override
    @Transactional
    public UserDTO updateUser(Long id, UserUpdateDTO userUpdateDTO) {

        AppUser user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        if (user.getEmail().equals(userUpdateDTO.email()) && userRepository.existsByEmail(userUpdateDTO.email())) {
            throw new EmailAlreadyExistException("Email already exists: " + userUpdateDTO.email());
        }

        user.setLogin(userUpdateDTO.login());
        user.setName(userUpdateDTO.name());
        user.setEmail(userUpdateDTO.email());

        AppUser updatedUser = userRepository.save(user);
        return convertEntityToDTO(updatedUser);

    }

    @Override
    @Transactional
    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)){
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);

    }
}
