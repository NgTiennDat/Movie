package com.datien.movie.user.controller;

import com.datien.movie.user.model.UserChangePassword;
import com.datien.movie.user.model.User;
import com.datien.movie.user.model.UserForgotPassword;
import com.datien.movie.user.service.UserService;
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

    @PatchMapping("/changePassword")
    public ResponseEntity<?> changePassword(
            @RequestBody UserChangePassword request,
            Authentication connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(
            @RequestBody UserForgotPassword userForgotPassword
    ) {
        userService.handleForgotPassword(userForgotPassword);
        return ResponseEntity.ok().build();
    }

}
