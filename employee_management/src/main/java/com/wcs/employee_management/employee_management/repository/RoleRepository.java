package com.wcs.employee_management.employee_management.repository;

import com.wcs.employee_management.employee_management.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    List<Role> findByIsActive(boolean isActive);

}
