package com.wcs.employee_management.employee_management.controller;

import com.wcs.employee_management.employee_management.exception.InvalidRoleException;
import com.wcs.employee_management.employee_management.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.wcs.employee_management.employee_management.entity.Role;

import java.util.List;

@Slf4j
@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> getAllRoles() {
        try {
            List<Role> roles = roleService.getAllRoles();
            return new ResponseEntity<>(roles, HttpStatus.OK);
        } catch (InvalidRoleException e) {
            log.error("Invalid Role " + e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Exception occured While getting roles" + e);
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
}
