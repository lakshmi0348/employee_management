package com.wcs.employee_management.employee_management.controller;

import com.wcs.employee_management.employee_management.DTO.ChangeExistingPassword;
import com.wcs.employee_management.employee_management.DTO.ChangePasswordRequest;
import com.wcs.employee_management.employee_management.DTO.LoginResponse;
import com.wcs.employee_management.employee_management.DTO.UserCreationRequest;
import com.wcs.employee_management.employee_management.exception.InvalidUserException;
import com.wcs.employee_management.employee_management.repository.UserRepository;
import com.wcs.employee_management.employee_management.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResetPasswordController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Operation(
            summary = "PasswordReset",
            description = "This Api will be used to reset the password ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Password has been reset successfully",
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
    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPasswordSecure(@RequestBody ChangePasswordRequest request) {
        try {
            userService.resetPasswordWithVerification(request);
            return ResponseEntity.ok("Password has been reset successfully.");
        } catch (InvalidUserException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User verification failed: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while resetting the password.");
        }
    }

    @Operation(
            summary = "PasswordChange",
            description = "This Api will be used to change the password ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Password has been changed successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Unauthorized request user not authenticated",
                            content = @Content
                    ),

            }
    )
    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangeExistingPassword request) {
        if (request == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
        }
        return userService.changePassword(request);
    }

}
