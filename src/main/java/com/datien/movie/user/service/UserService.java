package com.datien.movie.user.service;

import com.datien.movie.user.model.ChangePasswordRequest;
import com.datien.movie.user.model.User;
import com.datien.movie.user.daos.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No user found with id: " + userId));
    }

    public void changePassword(
            ChangePasswordRequest request,
            Authentication connectedUser
    ) {
        var user = (User) connectedUser.getPrincipal();

        // check if the current password is correct
        if(!passwordEncoder.matches(request.currentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }

        // check if the two new passwords are the same
        if(!request.newPassword().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("Password are not the same");
        }

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(user);
    }
}
