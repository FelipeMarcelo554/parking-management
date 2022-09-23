package com.api.parkingmanagement.domain.requests;

import com.api.parkingmanagement.domain.RoleModel;
import com.api.parkingmanagement.validations.annotations.UniqueValue;

import javax.validation.constraints.NotBlank;

public class RoleRequest {

    @NotBlank
    @UniqueValue(domainClass = RoleModel.class, fieldName = "name")
    private String roleName;

    public RoleRequest() {
    }

    public RoleRequest(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
