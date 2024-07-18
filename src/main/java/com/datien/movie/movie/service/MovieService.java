package com.datien.movie.movie.service;

import com.datien.movie.common.PageResponse;
import com.datien.movie.movie.daos.MovieRepository;
import com.datien.movie.movie.model.Movie;
import com.datien.movie.movie.model.MovieRequest;
import com.datien.movie.movie.model.MovieResponse;
import com.datien.movie.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieMapper mapper;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public Long savedMovie(MovieRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Movie movie = mapper.toMovie(request);
        movie.setOwner(user);
        movie.setCreatedDate(LocalDateTime.now());
        movie.setCreatedBy(user.getFullName());
        return movieRepository.save(movie).getId();
    }

    public MovieResponse getMovieId(Long movieId) {
        return movieRepository.findById(movieId)
                .map(mapper::toMovieResponse)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));
    }

    public PageResponse<MovieResponse> findAllMovies(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        Page<Movie> movies = movieRepository.findDisplayableMovies(pageable, user.getId());
        List<MovieResponse> movieResponses = movies.stream()
                .map(movieMapper::toMovieResponse)
                .toList();

        return new PageResponse<>(
                movieResponses,
                movies.getNumber(),
                movies.getSize(),
                movies.getTotalElements(),
                movies.getTotalPages(),
                movies.isFirst(),
                movies.isLast()
        );
    }

    public List<MovieResponse> findAllMovies2() {
        List<Movie> movies = movieRepository.findAll();
        return movies.stream()
                .map(movieMapper::toMovieResponse)
                .toList();
    }
}
