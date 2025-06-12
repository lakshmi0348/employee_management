package com.wcs.employee_management.employee_management.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeExistingPassword {

    private String email;
    private String oldPassword;
    private String newPassword;
}
