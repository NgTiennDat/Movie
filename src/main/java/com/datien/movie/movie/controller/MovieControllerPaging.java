package com.datien.movie.movie.controller;

import com.datien.movie.common.PageResponse;
import com.datien.movie.movie.model.MovieResponse;
import com.datien.movie.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v3/")
@RequiredArgsConstructor
public class MovieControllerPaging {

    public final MovieService movieService;


    @GetMapping("/movies")
    public ResponseEntity<PageResponse<MovieResponse>> findAllMovies(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            Authentication connectedUser
    ) {
        return ResponseEntity.ok(movieService.findAllMovies(page, size, connectedUser));
    }

    @GetMapping("/search/movies")
    public ResponseEntity<PageResponse<MovieResponse>> findMovieGenres(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "10", required = false) int size,
            @RequestParam(name = "genre") String genre
    ) {
        return ResponseEntity.ok(movieService.searchMoviesByGenresPaging(page, size, genre));
    }

}
