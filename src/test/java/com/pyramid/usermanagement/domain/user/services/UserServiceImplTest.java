package com.pyramid.usermanagement.domain.user.services;

import com.pyramid.usermanagement.core.exceptions.ResourceNotFoundException;
import com.pyramid.usermanagement.domain.user.dto.UserCreateDTO;
import com.pyramid.usermanagement.domain.user.dto.UserDTO;
import com.pyramid.usermanagement.domain.user.dto.UserUpdateDTO;
import com.pyramid.usermanagement.domain.user.model.AppUser;
import com.pyramid.usermanagement.domain.user.repository.UserRepository;
import com.pyramid.usermanagement.domain.user.services.implement.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private AppUser appUser;
    private UserCreateDTO userCreateDTO;
    private UserDTO userDTO;
    private List<AppUser> appUserList;
    private List<UserDTO> userList;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        appUser = new AppUser();
        appUser.setId(1L);
        appUser.setLogin("Lukaku");
        appUser.setName("Lukaku momba");
        appUser.setEmail("lukaku@yahoo.fr");

        AppUser appUser1 = new AppUser();
        appUser1.setId(2L);

        userCreateDTO = new UserCreateDTO("Lukaku", "Lukaku momba", "test@yahoo.fr");
        userDTO = new UserDTO(1L, "testuser", "testuser", "test@yahoo.fr", LocalDateTime.now());
        UserDTO userDTO2 = new UserDTO(2L, "dupont",
                "dupont", "dupont@yahoo.fr", LocalDateTime.now());
        userList = Arrays.asList(userDTO, userDTO2);


        appUserList = Arrays.asList(appUser, appUser1);

    }

    @Test
    void shouldCreateUser() {

        when(userRepository.save(any(AppUser.class))).thenReturn(appUser);

        UserDTO createdUser = userService.createUser(userCreateDTO);

        assertNotNull(createdUser);
        assertEquals(appUser.getId(), createdUser.Id());
        assertEquals(appUser.getEmail(), createdUser.email());
        verify(userRepository, times(1)).save(any(AppUser.class));

    }

    @Test
    void shouldGetAllUsers() {

        when(userRepository.findAll()).thenReturn(appUserList);

        List<UserDTO> result = userService.getAllUsers();

        assertNotEquals(0, result.size());
        verify(userRepository, times(1)).findAll();

    }

    @Test
    void shouldGetAllUsersPaginated() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<AppUser> userPage = new PageImpl<>(appUserList, pageable, appUserList.size());

        when(userRepository.findAll(any(Pageable.class))).thenReturn(userPage);

        Page<UserDTO> result = userService.getAllUsersPaginated(pageable);

        assertEquals(2, result.getTotalElements());
        verify(userRepository, times(1)).findAll(any(Pageable.class));

    }

    @Test
    void shouldGetUserByIdFound() {

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(appUser));

        UserDTO result = userService.getUserById(1L);

        assertEquals(appUser.getId(), result.Id());
        verify(userRepository, times(1)).findById(1L);

    }

    @Test
    void shouldRaiseExceptionWhenGetUserByIdFoundNotFound() {

        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserById(90L));

        assertEquals("User not found with id: 90", ex.getMessage());
        verify(userRepository, times(1)).findById(90L);

    }

    @Test
    void shouldGetUserByEmail() {

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(appUser));

        UserDTO result = userService.getUserByEmail("lukaku@yahoo.fr");

        assertEquals(appUser.getEmail(), result.email());
        verify(userRepository, times(1)).findByEmail(anyString());

    }

    @Test
    void shouldRaiseExceptionWhenGetUserByEmailNotFound() {

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> userService.getUserByEmail("gedeon@yahoo.fr"));

        assertEquals("User not found with email: gedeon@yahoo.fr", ex.getMessage());
        verify(userRepository, times(1)).findByEmail("gedeon@yahoo.fr");
    }

    @Test
    void shouldUpdateUser() {
        AppUser updateUser = new AppUser();
        updateUser.setId(1L);
        updateUser.setLogin("Lukaku");
        updateUser.setName("Lukaku kasongo");
        updateUser.setEmail("lukaku_kasongo@yahoo.fr");

        UserUpdateDTO userUpdateDTO = new UserUpdateDTO("Lukaku", "Lukaku kasongo", "lukaku_kasongo@yahoo.fr");

        when(userRepository.save(any(AppUser.class))).thenReturn(updateUser);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(appUser));

        UserDTO result = userService.updateUser(1L, userUpdateDTO);

        assertNotNull(result);
        assertEquals("lukaku_kasongo@yahoo.fr", result.email());
        assertEquals(1L, result.Id());
        verify(userRepository, times(1)).save(any(AppUser.class));
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    void shouldRaiseExceptionWhenUpdateUserNotFound() {

        when(userRepository.findById(anyLong())).thenThrow(new ResourceNotFoundException("User not found with id: 99"));

        UserUpdateDTO userUpdateDTO = new UserUpdateDTO("Lukaku", "Lukaku kasongo", "lukaku_kasongo@yahoo.fr");

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(99L, userUpdateDTO));

        assertEquals("User not found with id: 99", ex.getMessage());
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(0)).save(any(AppUser.class));

    }

    @Test
    void shouldDeleteUser() {

        doNothing().when(userRepository).deleteById(anyLong());
        when(userRepository.existsById(anyLong())).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).existsById(anyLong());
        verify(userRepository, times(1)).deleteById(anyLong());

    }

    @Test
    void shouldRaiseExceptionWhenDeleteUserNotFound() {

        //doThrow(new ResourceNotFoundException("User not found with id: 99")).when(userRepository)
        when(userRepository.existsById(anyLong())).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteUser(99L));

        assertEquals("User not found with id: 99", ex.getMessage());
        verify(userRepository, times(1)).existsById(anyLong());
        verify(userRepository, times(0)).deleteById(anyLong());

    }
}