package com.datien.movie.auth.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthenticationRequest {
    @NotBlank(message = "email is mandatory")
    @Email(message = "email not valid")
    private String email;

    @NotBlank(message = "password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum.")
    private String password;

}
