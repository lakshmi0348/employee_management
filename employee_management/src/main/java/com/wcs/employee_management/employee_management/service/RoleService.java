package com.wcs.employee_management.employee_management.service;

import com.wcs.employee_management.employee_management.entity.Role;
import java.util.List;

public interface RoleService {

    List<Role> getAllRoles();

    Role getRoleById(Integer id);
}
