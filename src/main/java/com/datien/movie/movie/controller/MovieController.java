package com.datien.movie.movie.controller;

import com.datien.movie.movie.model.MovieRequest;
import com.datien.movie.movie.model.MovieResponse;
import com.datien.movie.movie.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping("/movies")
    public ResponseEntity<Integer> savedMovie(
            @RequestBody @Valid MovieRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(movieService.savedMovie(request, connectedUser));
    }
}
