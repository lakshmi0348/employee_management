package com.wcs.employee_management.employee_management.service;

import com.wcs.employee_management.employee_management.entity.Role;
import com.wcs.employee_management.employee_management.entity.User;
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
                .orElseThrow(() -> new InvalidUserException("User not found with ID: " + id));
    }
}
