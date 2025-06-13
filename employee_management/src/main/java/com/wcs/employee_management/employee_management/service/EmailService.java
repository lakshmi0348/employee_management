package com.wcs.employee_management.employee_management.service;

public interface EmailService {
    void sendPasswordEmail(String toEmail, String password);
}