package com.wcs.employee_management.employee_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "wcs_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wcs00")
    @SequenceGenerator(name = "wcs00", sequenceName = "wcs00", allocationSize = 1)
    private Integer id;

    @Column(name = "full_name", columnDefinition = "varchar(200)", nullable = false)
    private String fullName;

    @Column(name = "last_name", columnDefinition = "varchar(100)")
    private String lastName;

    @Column(name = "password", columnDefinition = "text", nullable = false)
    private String password;

    @Column(name = "email", columnDefinition = "varchar(100)", unique = true)
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
}
