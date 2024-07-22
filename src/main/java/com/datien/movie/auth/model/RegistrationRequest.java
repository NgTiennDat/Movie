package com.datien.movie.auth.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
public class RegistrationRequest {

    @NotEmpty(message = "Firstname is mandatory.")
    @NotBlank(message = "Firstname is mandatory.")
    private String firstname;
    @NotEmpty(message = "Lastname is mandatory.")
    @NotBlank(message = "Lastname is mandatory.")
    private String lastname;
    @NotEmpty(message = "Email is mandatory.")
    @NotBlank(message = "Email is mandatory.")
    @Email(message = "Please, provide a valid email!")
    private String email;
    @NotEmpty(message = "Password is mandatory.")
    @NotBlank(message = "Password is mandatory.")
    @Size(min = 8, message = "Password should be 8 characters long minimum.")
    private String password;
}
