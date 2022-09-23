package com.api.parkingmanagement.domain.requests;

import com.api.parkingmanagement.domain.ParkingSpotModel;
import com.api.parkingmanagement.domain.UserModel;
import com.api.parkingmanagement.validations.annotations.UniqueValue;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class UserDto {

    @NotBlank
    @UniqueValue(domainClass = UserModel.class, fieldName = "userName")
    private String userName;

    @NotBlank
    @Min(6)
    private String password;

    public UserDto() {
    }

    public UserDto(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
