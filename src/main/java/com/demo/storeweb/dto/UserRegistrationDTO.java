package com.demo.storeweb.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.Set;

@Data
public class UserRegistrationDTO {
    @NotEmpty(message = "Yêu cầu nhập tài khoản")
    private String username;

    @NotEmpty(message = "Yêu cầu nhập mật khẩu")
    private String password;

    private Set<String> favoriteCategories;
}
