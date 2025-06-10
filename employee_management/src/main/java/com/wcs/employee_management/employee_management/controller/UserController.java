package com.wcs.employee_management.employee_management.controller;

import com.wcs.employee_management.employee_management.DTO.UserCreationRequest;
import com.wcs.employee_management.employee_management.DTO.UserResponseDTO;
import com.wcs.employee_management.employee_management.entity.User;
import com.wcs.employee_management.employee_management.exception.InvalidSearchQueryException;
import com.wcs.employee_management.employee_management.exception.InvalidUserException;
import com.wcs.employee_management.employee_management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController

public class UserController {


    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody UserCreationRequest userCreateRequest) {
        try {
            User createdUser = userService.createUser(userCreateRequest);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (InvalidUserException e) {
            log.warn("User creation failed: {}", e.getMessage());
            return new ResponseEntity<>("Unable to create user details", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Unexpected error during user creation", e);
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getUserById")
    public ResponseEntity<User> getUserById(@RequestParam(required = false) String id) {
        try {
            if (id == null || id.equalsIgnoreCase("null") || id.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            Integer parsedId = Integer.parseInt(id);
            User user = userService.getUserById(parsedId);
            return ResponseEntity.ok(user);
        } catch (InvalidUserException e) {
            log.warn("User not found for ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.error("Unexpected error while fetching user", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDTO>> searchUsers(@RequestParam String SearchText) {
        try {
            List<UserResponseDTO> users = userService.searchByUser(SearchText);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Unexpected error during user search", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (InvalidUserException e) {
            log.warn("Invalid user retrieval: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        } catch (Exception e) {
            log.error("Unexpected error while retrieving users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
