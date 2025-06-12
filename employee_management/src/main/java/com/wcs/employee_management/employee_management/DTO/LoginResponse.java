package com.wcs.employee_management.employee_management.DTO;

import com.wcs.employee_management.employee_management.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class LoginResponse {
    private Integer id;
    private String first_name;
    private String last_name;
    private String middleName;
    private String maidenName;
    private Integer zipcode;
    private String homeEmail;
    private String businessEmail;
    private Long homePhone;
    private Long businessPhone;
    private Long cellPhone;
    private String division;
    private String position;
    private String dutyType;
    private LocalDate hireDate;
    private LocalDate originalHireDate;
    private LocalDate terminationDate;
    private String terminationReason;
    private Boolean voluntaryTermination;
    private LocalDate rehireDate;
    private String rateType;
    private BigDecimal rate;
    private String payFrequency;
    private String payFrequencyText;
    private String homeDepartment;
    private String departmentText;

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
    private Long hourlyRate2;
    private Long hourlyRate3;
    private Long benefitClassCode;
    private String benefitDescription;
    private Long benefitAccrualRate;
    private String benefitStatus;
    private Long classCode;
    private String classDescription;
    private Long classAccrualRate;
    private String classStatus;
    private String supervisorName;
    private String supervisorReport;
    private Boolean isSupervisor;
    private byte[] photograph;
    private String maritalStatus;
    private String ethnicGroup;
    private String eeoClass;
    private String ssn;
    private Boolean workInState;
    private Boolean liveInState;
    private String citizenship;
    private String emergencyContact;
    private Long emergencyHomePhoneNumber;
    private Long emergencyWorkPhone;
    private String emergencyContactRelation;
    private Long alterEmergencyContact;
    private Long alterEmergencyHomePhoneNumber;
    private Long alterEmergencyWorkPhone;
    private String city;
    private String country;
}
