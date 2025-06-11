package com.wcs.employee_management.employee_management.DTO;

import com.wcs.employee_management.employee_management.entity.Role;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LoginResponse {
    private Integer id;
    private String name;
    private String last_name;
    private LocalDate date_of_birth;
    private String department;
    private String employment_type;
    private String job_title;
    private LocalDate date_of_joining;
    private String gender;
    private String email;
    private Long phone;
    private String token;
    private Role role;
    private Boolean isUserActive;

    public LoginResponse(Integer id, String name, String last_name, LocalDate date_of_birth, String department, String employment_type, String job_title, LocalDate date_of_joining, String gender, String email, Long phone, String token, Role role, Boolean isUserActive) {
        this.id = id;
        this.name = name;
        this.last_name = last_name;
        this.date_of_birth = date_of_birth;
        this.department = department;
        this.employment_type = employment_type;
        this.job_title = job_title;
        this.date_of_joining = date_of_joining;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.token = token;
        this.role = role;
        this.isUserActive = isUserActive;
    }
}
