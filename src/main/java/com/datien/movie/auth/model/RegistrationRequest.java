package com.datien.movie.auth.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
}
