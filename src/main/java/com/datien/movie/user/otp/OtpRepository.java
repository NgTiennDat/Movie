package com.datien.movie.user.otp;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByCode(String code);
    Optional<Otp> findByUserId(Long userId);
}