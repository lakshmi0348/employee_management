package com.wcs.employee_management.employee_management.DTO;

import com.wcs.employee_management.employee_management.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Getter
@Setter

public class UserCreationRequest {

    private String name;
    private String password;
    private Long phone;
    private String email;
    private Integer roleId;

    public User getUser() {
        User user = new User();
        user.setName(name);
        user.setPassword(encryptPassword(password));
        user.setEmail(email);
        user.setRoleId(roleId);
        user.setPhone(phone);
        return user;
    }


    private String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
