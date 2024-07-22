package com.datien.movie.movie.controller;

import com.datien.movie.movie.model.MovieRequest;
import com.datien.movie.movie.model.MovieResponse;
import com.datien.movie.movie.service.MovieRecommendationService;
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
    private final MovieRecommendationService movieRecommendationService;

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

    @GetMapping("/watch/{movie-id}")
    public ResponseEntity<MovieResponse> getMovieWatch(
            @PathVariable("movie-id") Long movieId,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(movieService.getMovieWatch(movieId, connectedUser));
    }

    @PutMapping("/movies/{movie-id}")
    public ResponseEntity<MovieResponse> updateMovie(
            @PathVariable("movie-id") Long movieId,
            @RequestBody @Valid MovieRequest request,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(movieService.updateMovie(movieId, request, connectedUser));
    }

    @DeleteMapping("/movies/{movie-id}")
    public void deleteMovie(
            @PathVariable("movie-id") Long movieId,
            Authentication connectedUser
    ) {
        movieService.deleteMovie(movieId, connectedUser);
    }

    @GetMapping("/movies/search")
    public ResponseEntity<List<MovieResponse>> searchMovies(
            @RequestParam String genres
    ) {
        return ResponseEntity.ok(movieService.searchMoviesByGenres(genres));
    }

    @GetMapping("/movies/top-rated")
    public ResponseEntity<List<MovieResponse>> getTopRatedMovies() {
        return ResponseEntity.ok(movieService.getTopRatedMovies());
    }

}
