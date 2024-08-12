package com.datien.movie.cinema;

import com.datien.movie.cinema.model.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    Optional<Cinema> findByCinemaName(String cinemaName);
}
