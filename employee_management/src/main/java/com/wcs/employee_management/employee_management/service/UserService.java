package com.wcs.employee_management.employee_management.service;


import com.wcs.employee_management.employee_management.DTO.UserCreationRequest;
import com.wcs.employee_management.employee_management.DTO.UserResponseDTO;
import com.wcs.employee_management.employee_management.entity.User;

import java.util.List;

public interface UserService {

    User createUser(UserCreationRequest request);

    User getUserById(Integer id);

    List<UserResponseDTO> searchByUser(String Query);

    List<User> getAllUsers();

}


