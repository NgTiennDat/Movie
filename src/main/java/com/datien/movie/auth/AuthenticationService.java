package com.datien.movie.auth;

import com.datien.movie.auth.model.AuthenticationRequest;
import com.datien.movie.auth.model.AuthenticationResponse;
import com.datien.movie.auth.model.RegistrationRequest;
import com.datien.movie.email.EmailService;
import com.datien.movie.role.RoleRepository;
import com.datien.movie.token.Token;
import com.datien.movie.token.TokenRepository;
import com.datien.movie.user.model.User;
import com.datien.movie.user.daos.UserRepository;
import com.datien.movie.util.JwtService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
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

    public void register(RegistrationRequest request) throws MessagingException {
        var userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("User Role Not Found"));

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .enabled(false)
                .roles(List.of(userRole))
                .build();

        userRepository.save(user);
        sendValidEmail(user);
    }

    public void sendValidEmail(User user) throws MessagingException {
        String activeToken = generateAndSavedActiveToken(user);

        emailService.sendEmail(
                user.getEmail(),
                user.getUsername(),
                "Account activation",
                activeToken
        );

        System.out.println(activeToken);
    }

    private String generateAndSavedActiveToken(User user) {
        String activeCode = generateActiveCode(6);

        var token = Token.builder()
                .token(activeCode)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .user(user)
                .build();
        tokenRepository.save(token);
        return activeCode;
    }

    private String generateActiveCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for(int i = 0; i < length; i++) {
            int randomInt = random.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomInt));
        }
        return codeBuilder.toString();
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

    public void activateAccount(String token) throws MessagingException {
        var savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("No token found."));

        if(savedToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            sendValidEmail(savedToken.getUser());
            throw new RuntimeException("Expired or invalid token");
        }

        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new RuntimeException("No user id found with id: " + savedToken.getUser().getId()));

        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }
}
