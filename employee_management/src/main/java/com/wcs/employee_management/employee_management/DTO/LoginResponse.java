package com.wcs.employee_management.employee_management.DTO;

import com.wcs.employee_management.employee_management.entity.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
    private Integer id;
    private String name;
    private String email;
    private Long phone;
    private String token;
    private Role role;

    public LoginResponse(Integer id, String name, String email, Long phone, String token, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.token = token;
        this.role = role;
    }
}
