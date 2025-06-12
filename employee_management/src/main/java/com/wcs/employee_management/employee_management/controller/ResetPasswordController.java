package com.wcs.employee_management.employee_management.controller;

import com.wcs.employee_management.employee_management.DTO.ChangeExistingPassword;
import com.wcs.employee_management.employee_management.DTO.ChangePasswordRequest;
import com.wcs.employee_management.employee_management.DTO.UserCreationRequest;
import com.wcs.employee_management.employee_management.entity.User;
import com.wcs.employee_management.employee_management.exception.InvalidUserException;
import com.wcs.employee_management.employee_management.repository.UserRepository;
import com.wcs.employee_management.employee_management.service.UserService;
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

    @PostMapping("/changePassword")
    public ResponseEntity<?> changePassword(@RequestBody ChangeExistingPassword request,
                                            UserCreationRequest userCreationRequest) {
        if (userCreationRequest == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated.");
        }
        return userService.changePassword(request, userCreationRequest);
    }

}
