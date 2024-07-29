package com.datien.movie.user.model;

public record ChangePasswordRequest (

     String currentPassword,
     String newPassword,
     String confirmPassword
) {
}
