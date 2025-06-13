package com.wcs.employee_management.employee_management.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordEmail(String toEmail, String password) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Your Temporary password for login into employee_management");
        message.setText("Here is your Temporary password: " + password);
        message.setFrom("lakshmidurga0519@gmail.com");
        mailSender.send(message);
    }
}

