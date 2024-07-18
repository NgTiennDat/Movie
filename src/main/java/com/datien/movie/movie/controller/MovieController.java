package com.datien.movie.movie.controller;

import com.datien.movie.common.PageResponse;
import com.datien.movie.movie.model.MovieRequest;
import com.datien.movie.movie.model.MovieResponse;
import com.datien.movie.movie.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class MovieController {

    private final MovieService movieService;

    @PostMapping("/movies")
    public ResponseEntity<Long> savedMovie(
            @RequestBody @Valid MovieRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(movieService.savedMovie(request, connectedUser));
    }

    @GetMapping("/movies/{movie-id}")
    public ResponseEntity<MovieResponse> getMovie(
            @PathVariable("movie-id") Long movieId
    ) {
        return ResponseEntity.ok(movieService.getMovieId(movieId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<MovieResponse>> getAllMovies() {
        return ResponseEntity.ok(movieService.findAllMovies2());
    }

    @GetMapping
    public ResponseEntity<PageResponse<MovieResponse>> findAllMovies(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(movieService.findAllMovies(page, size, connectedUser));
    }
}
