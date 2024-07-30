package com.datien.movie.auth;

import com.datien.movie.auth.model.AuthenticationRequest;
import com.datien.movie.auth.model.AuthenticationResponse;
import com.datien.movie.auth.model.RegistrationRequest;
import com.datien.movie.email.EmailService;
import com.datien.movie.role.RoleRepository;
import com.datien.movie.token.Token;
import com.datien.movie.token.TokenRepository;
import com.datien.movie.user.otp.OtpRepository;
import com.datien.movie.user.model.User;
import com.datien.movie.user.daos.UserRepository;
import com.datien.movie.util.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;
    private final OtpRepository otpRepository;

    public void register(RegistrationRequest request) throws MessagingException {
        var userRole = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Role Not Found"));

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false)
                .roles(List.of(userRole))
                .build();

        var savedUser = userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var token = Token.builder()
                .user(savedUser)
                .token(jwtToken)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
        emailService.sendValidEmail(user);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var claims = new HashMap<String, Object>();
        var user = ((User) auth.getPrincipal());

        claims.put("user", user.getFullName());

        String jwtToken = jwtService.generateToken(claims, (User) auth.getPrincipal());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .notification("You have successfully logged in.")
                .build();
    }

    public void activateAccount(String code) throws MessagingException {
        var savedCode = otpRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("No token found."));

        if(savedCode.getExpiredAt().isBefore(LocalDateTime.now())) {
            emailService.sendValidEmail(savedCode.getUser());
            throw new RuntimeException("Expired or invalid token");
        }

        var user = userRepository.findById(savedCode.getUser().getId())
                .orElseThrow(() -> new RuntimeException("No user id found with id: " + savedCode.getUser().getId()));

        user.setEnabled(true);
        userRepository.save(user);
        savedCode.setValidatedAt(LocalDateTime.now());
        otpRepository.save(savedCode);
    }

}
