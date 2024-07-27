package com.datien.movie.token;

import com.datien.movie.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "_token")
public class Token {

    @Id
    @GeneratedValue
    private Integer id;
    private String token;

//    private boolean expired;
//    private boolean revoked;

    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime validatedAt;

    @ManyToOne
    @JoinColumn(
            name = "user_id", nullable = false
    )
    private User user;
}
