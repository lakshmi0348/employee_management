package com.wcs.employee_management.employee_management.DTO;

import com.wcs.employee_management.employee_management.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter

public class UserCreationRequest {

    private String name;
    private String last_name;
    private String password;
    private Long phone;
    private String department;
    private String email;
    private String gender;
    private Integer roleId;
    private LocalDate date_of_birth;
    private LocalDate date_of_joining;
    private String employment_type;
    private String job_title;
    private boolean userActive;

    public User getUser() {
        User user = new User();
        user.setFullName(name);
        user.setLastName(last_name);
        user.setDateOfBirth(date_of_birth);
        user.setDepartment(department);
        user.setGender(gender);
        user.setDateOfJoining(date_of_joining);
        user.setEmploymentType(employment_type);
        user.setJobTitle(job_title);
        user.setPassword(encryptPassword(password));
        user.setEmail(email);
        user.setRoleId(roleId);
        user.setPhone(phone);
        user.setUserActive(userActive);
        return user;
    }


    private String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
