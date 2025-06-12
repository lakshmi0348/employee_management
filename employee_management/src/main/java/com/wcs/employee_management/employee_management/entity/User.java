package com.wcs.employee_management.employee_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "wcs_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    //  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wcs00")
    //@SequenceGenerator(name = "wcs00", sequenceName = "wcs00", allocationSize = 1)
    private Integer id;

    @Column(name = "first_name", columnDefinition = "varchar(200)", nullable = false)
    private String firstName;

    @Column(name = "last_name", columnDefinition = "varchar(100)")
    private String lastName;

    @Column(name = "password", columnDefinition = "text", nullable = false)
    private String password;

    @Column(name = "email_address", columnDefinition = "varchar(100)", unique = true)
    private String email;

    @Column(name = "phone", columnDefinition = "Numeric(10,0)")
    private Long phone;

    @Column(name = "role_id", columnDefinition = "int")
    private Integer roleId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_user_role_id"))
    private Role role;

    @Column(name = "employee_id", unique = true)
    private String employeeId;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender", columnDefinition = "varchar(10)")
    private String gender;

    @Column(name = "job_title", columnDefinition = "varchar(100)")
    private String jobTitle;

    @Column(name = "department", columnDefinition = "varchar(100)")
    private String department;

    @Column(name = "date_of_joining")
    private LocalDate dateOfJoining;

    @Column(name = "employment_type", columnDefinition = "varchar(50)")
    private String employmentType;

    @Column(name = "is_active")
    private boolean isUserActive = true;

    @Column(name = "hourly_rate3", columnDefinition = "numeric(10)")
    private Long hourlyRate3;

    @Column(name = "hourly_rate2", columnDefinition = "numeric(10)")
    private Long hourlyRate2;

    @Column(name = "benefit_class_code")
    private Long benefitClassCode;

    @Column(name = "benefit_description", columnDefinition = "text")
    private String benefitDescription;

    @Column(name = "benefit_accrual_rate")
    private Long benefitAccrualRate;

    @Column(name = "benefit_status", columnDefinition = "varchar(200)")
    private String benefitStatus;

    @Column(name = "class_code")
    private Long classCode;

    @Column(name = "class_description", columnDefinition = "text")
    private String classDescription;

    @Column(name = "class_accrual_rate")
    private Long classAccrualRate;

    @Column(name = "class_status", columnDefinition = "varchar(200)")
    private String classStatus;

    @Column(name = "supervisor_name", columnDefinition = "varchar(200)")
    private String supervisorName;

    @Column(name = "supervisor_report", columnDefinition = "varchar(200)")
    private String supervisorReport;

    @Column(name = "is_supervisor")
    private Boolean isSupervisor;

    @Column(name = "photograph")
    private byte[] photograph;

    @Column(name = "marital_status", columnDefinition = "varchar(200)")
    private String maritalStatus;

    @Column(name = "ethnic_group", columnDefinition = "varchar(200)")
    private String ethnicGroup;

    @Column(name = "eeo_class", columnDefinition = "text")
    private String eeoClass;

    @Column(name = "ssn", columnDefinition = "text")
    private String ssn;

    @Column(name = "work_in_state")
    private Boolean workInState;

    @Column(name = "live_in_state")
    private Boolean liveInState;

    @Column(name = "citizenship", columnDefinition = "varchar(200)")
    private String citizenship;

    @Column(name = "emergency_contact", columnDefinition = "varchar(200)")
    private String emergencyContact;

    @Column(name = "emergency_home_phone_number", columnDefinition = "numeric(10)")
    private Long emergencyHomePhoneNumber;

    @Column(name = "emergency_work_phone", columnDefinition = "numeric(10)")
    private Long emergencyWorkPhone;

    @Column(name = "emergency_contact_relation", columnDefinition = "varchar(200)")
    private String emergencyContactRelation;

    @Column(name = "alter_emergency_contact", columnDefinition = "numeric(10)")
    private Long alterEmergencyContact;

    @Column(name = "alter_emergency_home_phone_number", columnDefinition = "numeric(10)")
    private Long alterEmergencyHomePhoneNumber;

    @Column(name = "alter_emergency_work_phone", columnDefinition = "numeric(10)")
    private Long alterEmergencyWorkPhone;

    @Column(name = "middle_name", columnDefinition = "varchar(200)")
    private String middleName;

    @Column(name = "maiden_name", columnDefinition = "varchar(200)")
    private String maidenName;

    @Column(name = "zipcode", columnDefinition = "numeric(6)")
    private Integer zipcode;

    @Column(name = "home_email", columnDefinition = "varchar(100)")
    private String homeEmail;

    @Column(name = "business_email", columnDefinition = "varchar(100)")
    private String businessEmail;

    @Column(name = "home_phone", columnDefinition = "numeric(10)")
    private Long homePhone;

    @Column(name = "business_phone", columnDefinition = "numeric(10)")
    private Long businessPhone;

    @Column(name = "cell_phone", columnDefinition = "numeric(10)")
    private Long cellPhone;

    @Column(name = "division", columnDefinition = "varchar(100)")
    private String division;

    @Column(name = "position", columnDefinition = "varchar(100)")
    private String position;

    @Column(name = "duty_type", columnDefinition = "varchar(50)")
    private String dutyType;

    @Column(name = "hire_date")
    private LocalDate hireDate;

    @Column(name = "original_hire_date")
    private LocalDate originalHireDate;

    @Column(name = "termination_date")
    private LocalDate terminationDate;

    @Column(name = "termination_reason", columnDefinition = "text")
    private String terminationReason;

    @Column(name = "voluntary_termination")
    private Boolean voluntaryTermination;

    @Column(name = "rehire_date")
    private LocalDate rehireDate;

    @Column(name = "rate_type", columnDefinition = "varchar(50)")
    private String rateType;

    @Column(name = "rate", precision = 10, scale = 2)
    private BigDecimal rate;

    @Column(name = "pay_frequency", columnDefinition = "varchar(50)")
    private String payFrequency;

    @Column(name = "pay_frequency_text", columnDefinition = "varchar(100)")
    private String payFrequencyText;

    @Column(name = "home_department", columnDefinition = "varchar(100)")
    private String homeDepartment;

    @Column(name = "department_text", columnDefinition = "varchar(100)")
    private String departmentText;

    @Column(name = "city", columnDefinition = "varchar(100)")
    private String city;

    @Column(name = "country", columnDefinition = "varchar(100)")
    private String country;

}
