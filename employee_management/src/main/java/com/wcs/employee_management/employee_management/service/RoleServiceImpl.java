package com.wcs.employee_management.employee_management.service;

import com.wcs.employee_management.employee_management.entity.Role;
import com.wcs.employee_management.employee_management.entity.User;
import com.wcs.employee_management.employee_management.exception.InvalidRoleException;
import com.wcs.employee_management.employee_management.exception.InvalidUserException;
import com.wcs.employee_management.employee_management.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new InvalidRoleException("Role not found with ID: " + id));
    }

    public Role updateRole(Integer id, Role updatedRole) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new InvalidRoleException("Role not found with ID: " + id));

        existingRole.setCode(updatedRole.getCode());
        existingRole.setName(updatedRole.getName());
        return roleRepository.save(existingRole);
    }

    public void deleteRole(Integer id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new InvalidUserException("Role not found with ID: " + id));

        role.setActive(false);
        roleRepository.save(role);
    }

    public List<Role> getRolesByIsActive(boolean isActive) {
        return roleRepository.findByIsActive(isActive);
    }

    public Role partialChangeRole(Integer id, Role rolePatch) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with ID: " + id));

        // Update only non-null fields
        if (rolePatch.getName() != null) {
            existingRole.setName(rolePatch.getName());
        }

        if (rolePatch.getCode() != null) {
            existingRole.setCode(rolePatch.getCode());
        }

        // Add other fields here as needed

        return roleRepository.save(existingRole);
    }


}
