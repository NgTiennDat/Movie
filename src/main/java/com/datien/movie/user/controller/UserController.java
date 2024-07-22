package com.datien.movie.user.controller;

import com.datien.movie.user.model.User;
import com.datien.movie.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
