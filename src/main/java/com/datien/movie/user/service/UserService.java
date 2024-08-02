package com.datien.movie.user.service;

import com.datien.movie.email.EmailService;
import com.datien.movie.token.Token;
import com.datien.movie.token.TokenRepository;
import com.datien.movie.user.model.UserChangePassword;
import com.datien.movie.user.model.User;
import com.datien.movie.user.repo.UserRepository;
import com.datien.movie.user.model.UserForgotPassword;
import com.datien.movie.user.model.UserResetPassword;
import com.datien.movie.user.otp.OtpRepository;
import com.datien.movie.util.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;
    private final OtpRepository otpRepository;
    private final UserTokenService userTokenService;
    private final JwtService jwtService;

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

        var oldActivateCode = otpRepository.findByUserId(user.getId());

        if(oldActivateCode.isEmpty()) {
            throw new IllegalArgumentException("No user with id: " + user.getId());
        }
        otpRepository.delete(oldActivateCode.get());
        user.setEnabled(false);
        emailService.sendValidEmail(user);
    }

    public void handleResetPassword(UserResetPassword userResetPassword) throws MessagingException {
        User user = userRepository.findByEmail(userResetPassword.email())
                .orElseThrow(() -> new RuntimeException("No user with email: " + userResetPassword.email()));

        var activeCode = otpRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("No activate code found."));

        if(activeCode.getExpiredAt().isBefore(LocalDateTime.now())) {
            emailService.sendValidEmail(user);
            throw new IllegalArgumentException("No user with id: " + user.getId());
        }

        user.setPassword(passwordEncoder.encode(userResetPassword.newPassword()));
        userRepository.save(user);
        otpRepository.save(activeCode);
    }
}
