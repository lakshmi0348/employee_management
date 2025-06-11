package com.wcs.employee_management.employee_management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "wcs_role")
public class Role {
    @Id
    @Column(name = "id", columnDefinition = "int")
    private Integer id;

    @Column(name = "code", columnDefinition = "varchar(10)", unique = true)
    private String code;

    @Column(name = "name", columnDefinition = "varchar(20)")
    private String name;

    @Column(name = "is_active")
    private boolean isActive = true;
}
