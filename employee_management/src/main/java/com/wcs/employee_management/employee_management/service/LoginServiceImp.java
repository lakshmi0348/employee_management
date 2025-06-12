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

        LoginResponse response = new LoginResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                user.getMaidenName(),
                user.getZipcode(),
                user.getHomeEmail(),
                user.getBusinessEmail(),
                user.getHomePhone(),
                user.getBusinessPhone(),
                user.getCellPhone(),
                user.getDivision(),
                user.getPosition(),
                user.getDutyType(),
                user.getHireDate(),
                user.getOriginalHireDate(),
                user.getTerminationDate(),
                user.getTerminationReason(),
                user.getVoluntaryTermination(),
                user.getRehireDate(),
                user.getRateType(),
                user.getRate(),
                user.getPayFrequency(),
                user.getPayFrequencyText(),
                user.getHomeDepartment(),
                user.getDepartmentText(),
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
                user.isUserActive(),
                user.getHourlyRate3(),
                user.getHourlyRate2(),
                user.getBenefitClassCode(),
                user.getBenefitDescription(),
                user.getBenefitAccrualRate(),
                user.getBenefitStatus(),
                user.getClassCode(),
                user.getClassDescription(),
                user.getClassAccrualRate(),
                user.getClassStatus(),
                user.getSupervisorName(),
                user.getSupervisorReport(),
                user.getIsSupervisor(),
                user.getPhotograph(),
                user.getMaritalStatus(),
                user.getEthnicGroup(),
                user.getEeoClass(),
                user.getSsn(),
                user.getWorkInState(),
                user.getLiveInState(),
                user.getCitizenship(),
                user.getEmergencyContact(),
                user.getEmergencyHomePhoneNumber(),
                user.getEmergencyWorkPhone(),
                user.getEmergencyContactRelation(),
                user.getAlterEmergencyContact(),
                user.getAlterEmergencyHomePhoneNumber(),
                user.getAlterEmergencyWorkPhone(),
                user.getCity(),      // ✅ added city
                user.getCountry()    // ✅ added country
        );

        return response;
    }

}
