package com.datien.movie.movie.service;

import com.datien.movie.common.PageResponse;
import com.datien.movie.exception.OperationNotPermittedException;
import com.datien.movie.movie.repo.MovieRepository;
import com.datien.movie.movie.model.Movie;
import com.datien.movie.movie.model.MovieRequest;
import com.datien.movie.movie.model.MovieResponse;
import com.datien.movie.user.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovieService {

    private final MovieMapper mapper;
    private final MovieRepository movieRepository;
    private final MovieMapper movieMapper;

    public Long savedMovie(MovieRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        log.info("User: {}", user);
        Movie movie = mapper.toMovie(request);
        movie.setOwner(user);
        movie.setCreatedDate(LocalDateTime.now());
        movie.setCreatedBy(user.getId());
        log.info("Movie: {}", movie);
        return movieRepository.save(movie).getId();
    }


    public MovieResponse getMovieId(Long movieId) {
        return movieRepository.findById(movieId)
                .map(mapper::toMovieResponse)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));
    }

    public PageResponse<MovieResponse> findAllMovies(int page, int size, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        log.info("User: {}", user);
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

    public MovieResponse getMovieWatch(Long movieId, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        log.info("User: {}", user);

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found"));

        if(movie.isArchived()) {
            throw new OperationNotPermittedException("The movie is archived so you can not watch this at this moment.");
        }

        movie.setViewCount(movie.getViewCount() + 1);

        return mapper.toMovieResponse(movie);
    }

    public MovieResponse updateMovie(Long movieId, MovieRequest request, Authentication connectedUser) {

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + movieId));

        User user = (User) connectedUser.getPrincipal();
        if(Objects.equals(user.getId(), movie.getOwner().getId())) {
            throw new OperationNotPermittedException("You can not update movie's info of another.");
        }


        // Update movie details
        movie.setName(request.name());
        movie.setReleasedYear(request.releasedYear());
        movie.setDirectorName(request.directorName());
        movie.setGenres(request.genres());
        movie.setSynopsis(request.synopsis());
//        movie.setMovieCover(request.getMovieCover());
//        movie.setArchived(request.isArchived());

        // Save updated movie
        movieRepository.save(movie);

        // Convert to MovieResponse
        MovieResponse response = new MovieResponse();
        response.setId(movie.getId());
        response.setName(movie.getName());
        response.setReleasedYear(movie.getReleasedYear());
        response.setDirectorName(movie.getDirectorName());
        response.setGenres(movie.getGenres());
        response.setSynopsis(movie.getSynopsis());
//        response.setMovieCover(movie.getMovieCover());
        response.setViewedCount(movie.getViewCount());
        response.setArchived(movie.isArchived());
        response.setRate(movie.getRate());

        return response;
    }


    public void deleteMovie(Long movieId, Authentication connectedUser) {

        User user = (User) connectedUser.getPrincipal();
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + movieId));

        if(Objects.equals(user.getId(), movie.getOwner().getId())) {
            throw new OperationNotPermittedException("You can not delete movie's info of another.");
        }
        movieRepository.delete(movie);

    }

    public List<MovieResponse> searchMoviesByGenres(String genres) {
        List<Movie> movies = movieRepository.findByGenres(genres);

        return movies.stream()
                .map(movieMapper::toMovieResponse)
                .toList();
    }

    public PageResponse<MovieResponse> searchMoviesByGenresPaging(int page, int size, String genres) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createDate").descending());
        Page<Movie> movies = movieRepository.findByGenresPaging(pageable, genres);

        List<MovieResponse> movieResponses = movies.stream()
                .map(mapper::toMovieResponse)
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

    public List<MovieResponse> getTopRatedMovies() {
        List<Movie> movies = movieRepository.findByTopRating();
        return movies
                .stream()
                .map(mapper::toMovieResponse)
                .toList();
    }

}
