package com.wcs.employee_management.employee_management.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(name = "RoleResponse", description = "Response object representing a role in the system")
public class RoleResponse {

    @Schema(description = "Unique identifier of the role", example = "1")
    private Integer id;

    @Schema(description = "Unique role code", example = "ADMIN")
    private String code;

    @Schema(description = "Name of the role", example = "Administrator")
    private String name;

    @Schema(description = "Status indicating if the role is active", example = "true")
    private boolean isActive;
}
