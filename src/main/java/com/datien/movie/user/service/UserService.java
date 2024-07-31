package com.datien.movie.user.service;

import com.datien.movie.email.EmailService;
import com.datien.movie.token.TokenRepository;
import com.datien.movie.user.model.UserChangePassword;
import com.datien.movie.user.model.User;
import com.datien.movie.user.daos.UserRepository;
import com.datien.movie.user.model.UserForgotPassword;
import com.datien.movie.user.model.UserResetPassword;
import com.datien.movie.user.otp.OtpRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;
    private final OtpRepository otpRepository;

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No user found with id: " + userId));
    }

    public void changePassword(
            UserChangePassword request,
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

    public void handleForgotPassword(UserForgotPassword userForgotPassword) throws MessagingException {
        User user = userRepository.findByEmail(userForgotPassword.email())
                .orElseThrow(() -> new RuntimeException("No user with email: " + userForgotPassword.email()));
        emailService.sendValidEmail(user);
    }

    public void handleResetPassword(UserResetPassword userResetPassword) throws MessagingException {
        User user = userRepository.findByEmail(userResetPassword.email())
                .orElseThrow(() -> new RuntimeException("No user with email: " + userResetPassword.email()));

        var activeCode = otpRepository.findByUserId(user.getId())
                .filter(otp -> otp.getExpiredAt().isAfter(LocalDateTime.now()))
                .filter(otp -> otp.getCode().equals(userResetPassword.activateCode()))
                .orElseThrow(() -> new RuntimeException("No user with active code: " + userResetPassword.activateCode()));

        userRepository.save(user);
        otpRepository.delete(activeCode);
    }
}
