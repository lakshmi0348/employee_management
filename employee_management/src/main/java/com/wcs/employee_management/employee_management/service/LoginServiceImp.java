package com.wcs.employee_management.employee_management.service;

import com.wcs.employee_management.employee_management.DTO.LoginRequest;
import com.wcs.employee_management.employee_management.DTO.LoginResponse;
import com.wcs.employee_management.employee_management.entity.User;
import com.wcs.employee_management.employee_management.exception.InvalidUserException;
import com.wcs.employee_management.employee_management.repository.UserRepository;
import com.wcs.employee_management.employee_management.security.JWTUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class LoginServiceImp implements LoginService {

    @Autowired
    private UserRepository userServiceRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String email = loginRequest.getEmail().trim().toLowerCase();
        String rawPassword = loginRequest.getPassword();

        User user = userServiceRepository.findByEmail(email);

        if (user == null || !passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidUserException("Invalid email or password.");
        }

        String token = JWTUtil.generateToken(
                user.getEmail(),
                List.of(user.getRole().getCode())
        );

        return new LoginResponse(
                user.getId(),
                user.getFullName(),
                user.getLastName(),
                user.getDateOfBirth(),
                user.getDepartment(),
                user.getEmploymentType(),
                user.getJobTitle(),
                user.getDateOfJoining(),
                user.getGender(),
                user.getEmail(),
                user.getPhone(),
                token,
                user.getRole(),
                user.isUserActive()
        );
    }
}
