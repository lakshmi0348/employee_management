package com.wcs.employee_management.employee_management.service;

import com.wcs.employee_management.employee_management.DTO.LoginRequest;
import com.wcs.employee_management.employee_management.DTO.LoginResponse;

public interface LoginService {
    LoginResponse login(LoginRequest loginRequest);
}
