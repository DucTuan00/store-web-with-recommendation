package com.demo.storeweb.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserLoginDTO {
    @NotEmpty(message = "Username is required")
    private String username;

    @NotEmpty(message = "Password is required")
    private String password;
}
