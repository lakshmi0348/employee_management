package com.wcs.employee_management.employee_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "name", columnDefinition = "varchar(200)", nullable = false)
    private String name;

    @Column(name = "password", columnDefinition = "text", nullable = false)
    private String password;

    @Column(name = "email", columnDefinition = "varchar(100)")
    private String email;

    @Column(name = "phone", columnDefinition = "Numeric(10,0)")
    private Long phone;

    @Column(name = "role_id", columnDefinition = "int")
    private Integer roleId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_user_role_id"))
    private Role role;

}
