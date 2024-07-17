package com.datien.movie.movie.service;

import com.datien.movie.movie.model.Movie;
import com.datien.movie.movie.model.MovieRequest;
import org.springframework.stereotype.Service;

@Service
public class MovieMapper {

    public Movie toMovie(MovieRequest request) {
        Movie movie = new Movie();
        movie.setName(request.name());
    }
}
