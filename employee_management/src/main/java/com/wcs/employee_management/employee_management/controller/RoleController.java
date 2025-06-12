package com.wcs.employee_management.employee_management.controller;

import com.wcs.employee_management.employee_management.exception.InvalidRoleException;
import com.wcs.employee_management.employee_management.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.wcs.employee_management.employee_management.entity.Role;

import java.util.List;

@Slf4j
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getRolesByIsActive(@RequestParam(name = "active", required = false) Boolean active) {
        try {
            List<Role> roles;

            if (active != null) {
                roles = roleService.getRolesByIsActive(active);
            } else {
                roles = roleService.getAllRoles(); // fallback to return all roles
            }

            return new ResponseEntity<>(roles, HttpStatus.OK);

        } catch (InvalidRoleException e) {
            log.error("Invalid Role: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Exception occurred while getting roles: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/getRoleById")
    public ResponseEntity<Role> getRoleById(@RequestParam(required = false) String id) {
        try {
            if (id == null || id.equalsIgnoreCase("null") || id.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(null);
            }
            Integer parsedId = Integer.parseInt(id);
            Role role = roleService.getRoleById(parsedId);
            return new ResponseEntity<>(role, HttpStatus.OK);
        } catch (InvalidRoleException e) {
            log.error("Invalid RoleID " + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            log.error("Exception occured While getting roleId" + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/updateRole/{id}")
    public ResponseEntity<?> updateRole(@PathVariable Integer id, @RequestBody Role role) {
        try {
            Role updatedRole = roleService.updateRole(id, role);
            return ResponseEntity.ok(updatedRole);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating role: " + e.getMessage());
        }
    }

    @PatchMapping("/updateRole/{id}")
    public ResponseEntity<?> patchRole(@PathVariable Integer id, @RequestBody Role role) {
        try {
            Role updatedRole = roleService.partialChangeRole(id, role);
            return ResponseEntity.ok(updatedRole);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error patching role: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteRole/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Integer id) {
        try {
            roleService.deleteRole(id);
            return ResponseEntity.ok("Role marked as INACTIVE");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting role: " + e.getMessage());
        }
    }

}
