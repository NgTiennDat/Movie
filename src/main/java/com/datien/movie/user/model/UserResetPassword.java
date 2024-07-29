package com.datien.movie.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserResetPassword(
        @NotEmpty(message = "Password is mandatory.")
        @NotBlank(message = "Password is mandatory.")
        @Email(message = "email not valid")
        String email,

        @NotBlank(message = "email is mandatory")
        @Size(min = 6, max = 6, message = "otp length must be 6")
        String activateCode,

        @NotEmpty(message = "Password is mandatory.")
        @NotBlank(message = "Password is mandatory.")
        @Size(min = 8, message = "Password should be 8 characters long minimum.")
        String newPassword,
        @NotEmpty(message = "Password is mandatory.")
        @NotBlank(message = "Password is mandatory.")
        @Size(min = 8, message = "Password should be 8 characters long minimum.")
        String confirmNewPassword
) {
}
