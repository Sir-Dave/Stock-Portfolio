package com.sirdave.portfolio.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ResetPasswordRequest {
    private String oldPassword;
    private String newPassword;
    private String confirmPassword;
}
