package com.datien.movie.movie.service;

import com.datien.movie.movie.model.Movie;
import com.datien.movie.movie.model.MovieRequest;
import com.datien.movie.movie.model.MovieResponse;
import org.springframework.stereotype.Service;

@Service
public class MovieMapper {

    public Movie toMovie(MovieRequest request) {
        Movie movie = new Movie();
        movie.setId(request.id());
        movie.setName(request.name());
        movie.setReleasedYear(request.releasedYear());
        movie.setDirectorName(request.directorName());
        movie.setIsbn(request.isbn());
        movie.setSynopsis(request.synopsis());
        movie.setArchived(false);

        return movie;
    }

    public MovieResponse toMovieResponse(Movie movie) {
        return MovieResponse.builder()
                .id(movie.getId())
                .name(movie.getName())
                .releasedYear(movie.getReleasedYear())
                .directorName(movie.getDirectorName())
                .isbn(movie.getIsbn())
                .synopsis(movie.getSynopsis())
                .archived(movie.isArchived())
                .build();
    }
}
