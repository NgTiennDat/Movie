package com.datien.movie.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserForgotPassword (
    @NotBlank(message = "email is mandatory")
    @Email(message = "email not valid")
    String email
) {
}
