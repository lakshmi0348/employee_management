package com.wcs.employee_management.employee_management.controller;

import com.wcs.employee_management.employee_management.DTO.LoginResponse;
import com.wcs.employee_management.employee_management.exception.InvalidRoleException;
import com.wcs.employee_management.employee_management.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(
            summary = "Get the roles by Active status",
            description = "This Api will be used to get the roles details by Id by using the Active status",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Getting roles by Active status",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid credentials (bad request)",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content
                    )
            }
    )
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

    @Operation(
            summary = "Get Role details by Id",
            description = "This Api will be used to get the role details by Id ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Getting role details successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid Role_id (bad request)",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content
                    )
            }
    )
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
    @Operation(
            summary = "Update Role",
            description = "This Api will be used to update the role details ",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Update role_details  successful",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content
                    )
            }
    )
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

    @Operation(
            summary = "partialUpdate role",
            description = "This Api will be used to update role By using role_id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "updated role details successful",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content
                    )
            }
    )
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
    @Operation(
            summary = "Delete Role",
            description = "This Api will be used to delete roles by using role_id navigating roleActive status into False",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Deleted successfully",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = LoginResponse.class))
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error",
                            content = @Content
                    )
            }
    )
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
