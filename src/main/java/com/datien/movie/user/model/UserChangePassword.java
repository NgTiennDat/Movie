package com.datien.movie.user.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserChangePassword(
        @NotEmpty(message = "Password is mandatory.")
        @NotBlank(message = "Password is mandatory.")
        @Size(min = 8, message = "Password should be 8 characters long minimum.")
        String currentPassword,
        @NotEmpty(message = "Password is mandatory.")
        @NotBlank(message = "Password is mandatory.")
        @Size(min = 8, message = "Password should be 8 characters long minimum.")
        String newPassword,
        @NotEmpty(message = "Password is mandatory.")
        @NotBlank(message = "Password is mandatory.")
        @Size(min = 8, message = "Password should be 8 characters long minimum.")
        String confirmPassword
) {
}
