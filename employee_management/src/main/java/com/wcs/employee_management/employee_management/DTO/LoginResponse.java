package com.wcs.employee_management.employee_management.DTO;

import com.wcs.employee_management.employee_management.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    @Schema(description = "User ID", example = "1")
    private Integer id;

    @Schema(description = "First name of the employee", example = "John")
    private String firstName;

    @Schema(description = "Last name of the employee", example = "Doe")
    private String lastName;

    @Schema(description = "Email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "User's phone number", example = "9876543210")
    private Long phone;

    @Schema(description = "Authentication token")
    private String token;

    @Schema(description = "User role", example = "ADMIN or USER")
    private Role role;

    @Schema(description = "Whether the user is active", example = "true")
    private Boolean isUserActive;
}
