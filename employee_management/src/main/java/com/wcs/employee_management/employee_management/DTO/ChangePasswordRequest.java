package com.wcs.employee_management.employee_management.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest {
    private String email;
    private String dateOfBirth;
    private String phoneNumber;
    private String newPassword;
}

