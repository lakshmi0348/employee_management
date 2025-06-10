package com.wcs.employee_management.employee_management.controller;

import com.wcs.employee_management.employee_management.DTO.LoginRequest;
import com.wcs.employee_management.employee_management.exception.InvalidUserException;
import com.wcs.employee_management.employee_management.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        try {
            return new ResponseEntity<>(loginService.login(loginRequest), HttpStatus.OK);
        } catch (InvalidUserException e) {
            log.error("Invalid User Exception " + e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Exception while login "+e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
