package com.demo.storeweb.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserLoginDTO {
    @NotEmpty(message = "Yêu cầu nhập tài khoản")
    private String username;

    @NotEmpty(message = "Yêu cầu nhập mật khẩu")
    private String password;
}
