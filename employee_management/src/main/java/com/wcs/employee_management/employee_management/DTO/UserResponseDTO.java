package com.wcs.employee_management.employee_management.DTO;

import com.wcs.employee_management.employee_management.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {

    private Integer id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private Long phone;
    private String employeeId;
    private String jobTitle;
    private String department;
    private String employmentType;
    private String roleName;
    private String roleCode;
    private String supervisorName;
    private Boolean isSupervisor;
    private Long hourlyRate2;
    private Long hourlyRate3;
    private String benefitDescription;
    private String benefitStatus;
    private String classDescription;
    private String classStatus;
    private String maritalStatus;
    private String ethnicGroup;
    private String citizenship;
    private Integer zipcode;
    private String ssn;
    private String eeoClass;
    private Boolean workInState;
    private Boolean liveInState;
    private String emergencyContact;
    private Long emergencyHomePhoneNumber;
    private Long emergencyWorkPhone;
    private String emergencyContactRelation;
    private Long alterEmergencyContact;
    private Long alterEmergencyHomePhoneNumber;
    private Long alterEmergencyWorkPhone;

    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.fullName = user.getFirstName() + " " + user.getLastName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.employeeId = user.getEmployeeId();
        this.jobTitle = user.getJobTitle();
        this.department = user.getDepartment();
        this.employmentType = user.getEmploymentType();
        this.roleName = user.getRole() != null ? user.getRole().getName() : null;
        this.roleCode = user.getRole() != null ? user.getRole().getCode() : null;
        this.supervisorName = user.getSupervisorName();
        this.isSupervisor = user.getIsSupervisor();
        this.hourlyRate2 = user.getHourlyRate2();
        this.hourlyRate3 = user.getHourlyRate3();
        this.benefitDescription = user.getBenefitDescription();
        this.benefitStatus = user.getBenefitStatus();
        this.classDescription = user.getClassDescription();
        this.classStatus = user.getClassStatus();
        this.maritalStatus = user.getMaritalStatus();
        this.ethnicGroup = user.getEthnicGroup();
        this.citizenship = user.getCitizenship();
        this.zipcode = user.getZipcode();
        this.ssn = user.getSsn();
        this.eeoClass = user.getEeoClass();
        this.workInState = user.getWorkInState();
        this.liveInState = user.getLiveInState();
        this.emergencyContact = user.getEmergencyContact();
        this.emergencyHomePhoneNumber = user.getEmergencyHomePhoneNumber();
        this.emergencyWorkPhone = user.getEmergencyWorkPhone();
        this.emergencyContactRelation = user.getEmergencyContactRelation();
        this.alterEmergencyContact = user.getAlterEmergencyContact();
        this.alterEmergencyHomePhoneNumber = user.getAlterEmergencyHomePhoneNumber();
        this.alterEmergencyWorkPhone = user.getAlterEmergencyWorkPhone();
    }
}
