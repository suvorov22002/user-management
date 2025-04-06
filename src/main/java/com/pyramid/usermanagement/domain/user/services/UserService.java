package com.pyramid.usermanagement.domain.user.services;

import com.pyramid.usermanagement.domain.user.dto.UserCreateDTO;
import com.pyramid.usermanagement.domain.user.dto.UserDTO;
import com.pyramid.usermanagement.domain.user.dto.UserUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    UserDTO createUser(UserCreateDTO userCreateDTO);
    List<UserDTO> getAllUsers();
    Page<UserDTO> getAllUsersPaginated(Pageable pageable);
    UserDTO getUserById(Long id);
    UserDTO getUserByEmail(String email);
    UserDTO updateUser(Long id, UserUpdateDTO userUpdateDTO);
    void deleteUser(Long id);

}
