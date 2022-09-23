package com.api.parkingmanagement.domain.requests;

import javax.validation.constraints.NotBlank;

public class RoleToUserRequest {

    @NotBlank
    private String userName;
    @NotBlank
    private String roleName;

    public RoleToUserRequest() {
    }

    public RoleToUserRequest(String userName, String roleName) {
        this.userName = userName;
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
