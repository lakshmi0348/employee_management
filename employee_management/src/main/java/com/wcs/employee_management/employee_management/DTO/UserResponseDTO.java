package com.wcs.employee_management.employee_management.DTO;

import com.wcs.employee_management.employee_management.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class UserResponseDTO {
    private Integer id;
    private String name;
    private String email;
    private Long phone;
    private String roleName;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.roleName = user.getRole() != null ? user.getRole().getName() : null;
    }
}
