package com.wcs.employee_management.employee_management.controller;

import com.wcs.employee_management.employee_management.DTO.LoginResponse;
import com.wcs.employee_management.employee_management.DTO.UserCreationRequest;
import com.wcs.employee_management.employee_management.DTO.UserResponseDTO;
import com.wcs.employee_management.employee_management.entity.User;
import com.wcs.employee_management.employee_management.exception.InvalidUserException;
import com.wcs.employee_management.employee_management.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
            summary = "User Login",
            description = "This Api will be used to create the userDetails Admin only access this Api using token ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "user creation successful",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid credentials (bad request)",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content
                    )
            }
    )
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
    @Operation(
            summary = "GET user By Id",
            description = "This Api will be used to get  the userDetails By Id  this Api will be accessed by using token ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "all users retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid credentials (bad request)",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content
                    )
            }
    )
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

    @Operation(
            summary = "Search Functionality",
            description = "This Api will be used to search the userDetails  only accessing by using Api using token ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Search text is found",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content
                    )
            }
    )
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
    @Operation(
            summary = "Get Users",
            description = "This Api will be used to get the userDetails  only accessing by using Api using token ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Getting the user details",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content
                    )
            }
    )


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

    @Operation(
            summary = "Update the users",
            description = "This Api will be used to update the userDetails by id only accessing by using Api using token ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "updating User details",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content
                    )
            }
    )
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


    @Operation(
            summary = "partialUpdate the users",
            description = "This Api will be used to update the userDetails by id only accessing by using Api using token ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "updating User details",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content
                    )
            }
    )
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
    @Operation(
            summary = "Delete the users",
            description = "This Api will be used to delete  the userDetails by navigating userActive status accessing by using Api using token ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deleting the user details By migrating Active status ",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content
                    )
            }
    )
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
