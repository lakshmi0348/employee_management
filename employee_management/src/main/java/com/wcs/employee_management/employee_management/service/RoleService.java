package com.wcs.employee_management.employee_management.service;

import com.wcs.employee_management.employee_management.entity.Role;
import java.util.List;

public interface RoleService {

    List<Role> getAllRoles();

    Role getRoleById(Integer id);

    Role updateRole(Integer id, Role updatedRole);

    void deleteRole(Integer id);

    List<Role> getRolesByIsActive(boolean isActive);

    Role partialChangeRole(Integer id, Role role);
}
