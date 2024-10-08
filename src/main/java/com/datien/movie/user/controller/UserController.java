package com.datien.movie.user.controller;

import com.datien.movie.user.model.UserChangePassword;
import com.datien.movie.user.model.User;
import com.datien.movie.user.model.UserForgotPassword;
import com.datien.movie.user.model.UserResetPassword;
import com.datien.movie.user.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v3/user")
public class UserController {

    public final UserService userService;

    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.getAllUser();
    }

    @GetMapping("/{userId}")
    public User getUserById(
            @PathVariable("userId") Long userId
    ) {
        return userService.findUserById(userId);
    }

    @PatchMapping("/password/change")
    public ResponseEntity<?> changePassword(
            @RequestBody UserChangePassword request,
            Authentication connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/forgot")
    public ResponseEntity<?> forgotPassword(
            @RequestBody UserForgotPassword userForgotPassword
    ) throws MessagingException {
        userService.handleForgotPassword(userForgotPassword);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/password/reset")
    public ResponseEntity<?> resetPassword(
        @Valid @RequestBody UserResetPassword userResetPassword
    ) throws MessagingException {
        userService.handleResetPassword(userResetPassword);
        return ResponseEntity.ok().build();
    }
}
