package com.datien.movie.movie.service;

import com.datien.movie.movie.model.Movie;
import com.datien.movie.movie.model.MovieRequest;
import com.datien.movie.movie.model.MovieResponse;
import com.datien.movie.user.User;
import com.datien.movie.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieMapper mapper;
    private final UserRepository userRepository;

    public Integer savedMovie(MovieRequest request, Authentication connectedUser) {
        User user = (User) connectedUser.getPrincipal();
        Movie movie =
        return null;
    }
}
