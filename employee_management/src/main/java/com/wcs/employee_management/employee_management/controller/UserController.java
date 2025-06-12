package com.wcs.employee_management.employee_management.controller;

import com.wcs.employee_management.employee_management.DTO.UserCreationRequest;
import com.wcs.employee_management.employee_management.DTO.UserResponseDTO;
import com.wcs.employee_management.employee_management.entity.User;
import com.wcs.employee_management.employee_management.exception.InvalidUserException;
import com.wcs.employee_management.employee_management.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


@Slf4j
@RestController

public class UserController {


    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Object> createUser(@RequestBody UserCreationRequest userCreateRequest) {
        try {
            User createdUser = userService.createUser(userCreateRequest);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (InvalidUserException ex) {
            return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);
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
    public ResponseEntity<List<UserResponseDTO>> searchUsers(@RequestParam String searchText) {
        try {
            List<UserResponseDTO> users = userService.searchByUser(searchText);
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            log.error("Unexpected error during user search", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getUsers")
    public ResponseEntity<List<User>> getUsers(@RequestParam(required = false) Boolean isActive) {
        try {
            List<User> users;

            if (isActive == null) {
                // No filter: return all users
                users = userService.getAllUsers();
            } else {
                // Filter by active status
                users = userService.findByIsActive(isActive);
            }

            return ResponseEntity.ok(users);
        } catch (InvalidUserException e) {
            log.warn("Invalid user status retrieval: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
        } catch (Exception e) {
            log.error("Unexpected error while retrieving users", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/updateUsers/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody User user) {
        try {
            User updated = userService.updateUser(id, user);
            return ResponseEntity.ok(updated);
        } catch (InvalidUserException e) {
            log.error("Invalid user: " + e.getMessage());
            return ResponseEntity.badRequest().body("Invalid user: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error occurred while updating user: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while updating the user.");
        }
    }

    @PatchMapping("/updateUsers/{id}")
    public ResponseEntity<?> patchUser(@PathVariable Integer id, @RequestBody User userPatch) {
        try {
            User updated = userService.partialChangeUser(id, userPatch);
            return ResponseEntity.ok(updated);
        } catch (InvalidUserException e) {
            log.error("Invalid user: " + e.getMessage());
            return ResponseEntity.badRequest().body("Invalid user: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error occurred while patching user: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while patching the user.");
        }
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        try {
            userService.markUserAsInactive(id);
            return ResponseEntity.ok("User marked as INACTIVE");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error marking user as INACTIVE: " + e.getMessage());
        }
    }
}
