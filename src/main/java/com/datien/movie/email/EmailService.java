package com.datien.movie.email;

import com.datien.movie.token.Token;
import com.datien.movie.token.TokenRepository;
import com.datien.movie.user.model.User;
import com.datien.movie.user.otp.Otp;
import com.datien.movie.user.otp.OtpRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final TokenRepository tokenRepository;
    private final OtpRepository otpRepository;

    @Async
    public void sendEmail(
            String to,
            String username,
            String subject,
            String activationCode
    ) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(
                message,
                MimeMessageHelper.MULTIPART_MODE_MIXED,
                UTF_8.name()
        );
        Map<String, Object> properties = new HashMap<>();
        properties.put("username", username);
        properties.put("activation_code", activationCode);

        Context context = new Context();
        context.setVariables(properties);

        helper.setFrom("ntdat14092003@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText("mail to activate the registered account. The active code is " + activationCode);

        mailSender.send(message);
        System.out.println("mail sent successfully...");
    }

    @Async
    public void sendValidEmail(User user) throws MessagingException {
        String activeCode = generateAndSavedActiveToken(user);

        this.sendEmail(
                user.getEmail(),
                user.getUsername(),
                "Account activation",
                activeCode
        );

        System.out.println(activeCode);
    }

    private String generateAndSavedActiveToken(User user) {
        String activeCode = generateActiveCode(6);

        var otp = Otp.builder()
                .code(activeCode)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(5))
                .user(user)
                .build();
        otpRepository.save(otp);
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
}
