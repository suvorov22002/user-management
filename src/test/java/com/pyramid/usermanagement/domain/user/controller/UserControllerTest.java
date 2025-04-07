package com.pyramid.usermanagement.domain.user.controller;

import com.pyramid.usermanagement.core.exceptions.GlobalExceptionHandler;
import com.pyramid.usermanagement.core.exceptions.ResourceNotFoundException;
import com.pyramid.usermanagement.domain.user.dto.UserCreateDTO;
import com.pyramid.usermanagement.domain.user.dto.UserDTO;
import com.pyramid.usermanagement.domain.user.dto.UserUpdateDTO;
import com.pyramid.usermanagement.domain.user.model.AppUser;
import com.pyramid.usermanagement.domain.user.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @InjectMocks
    private UserController userController;

    private AppUser testUser;
    private UserDTO userDTO;
    private UserCreateDTO userCreateDTO;
    private List<UserDTO> userList;

    @BeforeEach
    void setup() {

        MockitoAnnotations.openMocks(this);

        testUser = new AppUser();
        testUser.setId(1L);
        testUser.setName("testuser");
        testUser.setEmail("test@yahoo.fr");
        testUser.setLogin("testuser");

        userCreateDTO = new UserCreateDTO("testuser", "testuser", "test@yahoo.fr");
        userDTO = new UserDTO(1L, "testuser", "testuser", "test@yahoo.fr", LocalDateTime.now());
        UserDTO userDTO2 = new UserDTO(2L, "dupont",
                "dupont", "dupont@yahoo.fr", LocalDateTime.now());
        userList = Arrays.asList(userDTO, userDTO2);

    }

    @Test
    void shouldCreateNewUser() {

        when(userService.createUser(any(UserCreateDTO.class))).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.createUser(userCreateDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
        verify(userService, times(1)).createUser(any(UserCreateDTO.class));

    }

    @Test
    void shouldGetAllUsers() {

        when(userService.getAllUsers()).thenReturn(userList);

        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(userService, times(1)).getAllUsers();

    }

    @Test
    void shoudGetAllUserPaginated() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<UserDTO> userPage = new PageImpl<>(userList, pageable, userList.size());

        when(userService.getAllUsersPaginated(any(Pageable.class))).thenReturn(userPage);

        ResponseEntity<Page<UserDTO>> response = userController
                .getPaginatedUsers(0, 10, "id", "asc");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getTotalElements());
        verify(userService, times(1)).getAllUsersPaginated(any(Pageable.class));

    }

    @Test
    void shouldGetUserByIdFound() {

        when(userService.getUserById(anyLong())).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
        verify(userService, times(1)).getUserById(1L);
    }

    @Test
    void shouldGetUserByIdNotFound() {

        when(userService.getUserById(anyLong()))
                .thenThrow(new ResourceNotFoundException("User not found with id: 2"));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> userController.getUserById(2L));

        assertEquals("User not found with id: 2", ex.getMessage());
        verify(userService, times(1)).getUserById(2L);

    }

    @Test
    void shouldGetUserByEmailFound() {

        when(userService.getUserByEmail(anyString())).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = userController.getUserByEmail("test@yahoo.fr");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userDTO, response.getBody());
        verify(userService, times(1)).getUserByEmail("test@yahoo.fr");

    }

    @Test
    void shouldGetUserByEmailNotFound() {

        when(userService.getUserByEmail(anyString()))
                .thenThrow(new ResourceNotFoundException("User not found with email: dupont@yahoo.fr"));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> userController.getUserByEmail("dupont@yahoo.fr"));

        assertEquals("User not found with email: dupont@yahoo.fr", ex.getMessage());
        verify(userService, times(1)).getUserByEmail("dupont@yahoo.fr");
    }

    @Test
    void shouldUpdateUserFound() {

        when(userService.updateUser(anyLong(), any(UserUpdateDTO.class))).thenReturn(userDTO);

        UserUpdateDTO userUpdateDTO = new UserUpdateDTO("test", "lyly rush", "rush@gmail.com");

        ResponseEntity<UserDTO> response = userController.updateUser(1L, userUpdateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).updateUser(anyLong(), any(UserUpdateDTO.class));

    }

    @Test
    void shouldRaiseExceptionWhenUpdateUserNotFound() {

        when(userService.updateUser(anyLong(), any(UserUpdateDTO.class)))
                .thenThrow(new ResourceNotFoundException("User not found with id: 99"));

        UserUpdateDTO updateDTO = new UserUpdateDTO("allen", "allen", "allenB@yahoo.com");

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> userService.updateUser(99L, updateDTO));

        assertEquals("User not found with id: 99", ex.getMessage());
    }

    @Test
    void shouldDeleteUserFound() {

        doNothing().when(userService).deleteUser(anyLong());

        ResponseEntity<Void> response = userController.deleteUser(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(userService, times(1)).deleteUser(anyLong());

    }

    @Test
    void shouldRaiseExceptionWhenDeleteUserNotFound() {

        doThrow(new ResourceNotFoundException("User not found with id: 99")).when(userService).deleteUser(anyLong());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> userController.deleteUser(99L));

        assertEquals("User not found with id: 99", ex.getMessage());

    }
}