package com.datien.movie.feedback.service;

import com.datien.movie.feedback.daos.FeedbackRepository;
import com.datien.movie.feedback.model.Feedback;
import com.datien.movie.feedback.model.FeedbackRequest;
import com.datien.movie.feedback.model.FeedbackResponse;
import com.datien.movie.movie.daos.MovieRepository;
import com.datien.movie.movie.model.Movie;
import com.datien.movie.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository repository;
    private final FeedbackMapper mapper;
    private final MovieRepository movieRepository;
    private final FeedbackRepository feedbackRepository;

    public Long addFeedback(FeedbackRequest request, Authentication connectedUser) {

        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        if(movie.isArchived()) {
            throw new IllegalStateException("Archived movie is not allowed.");
        }

        if(!connectedUser.isAuthenticated()) {
            throw new SecurityException("User is not authenticated.");
        }

        User user = (User) connectedUser.getPrincipal();

        if(Objects.equals(movie.getOwner().getId(), user.getId())) {
            throw new IllegalArgumentException("You can not add feedback to your own film");
        }
    
        Feedback feedback = mapper.toFeedback(request);
        feedback.setMovie(movie);
        feedback.setCreatedDate(LocalDateTime.now());
        feedback.setCreatedBy(user.getId());
        repository.save(feedback);
        return feedback.getId();
    }

    public List<FeedbackResponse> getAllFeedbacksOfMovie(Long movieId, Authentication connectedUser) {

        User user = (User) connectedUser.getPrincipal();

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("No movie found with id: " + movieId));

        List<Feedback> feedbacks = feedbackRepository.findAllFeedbackByMovieId(movie.getId());

        return feedbacks.stream()
                .map(feedback -> mapper.toFeedbackResponse(feedback, user.getId()))
                .toList();
    }

    public void updateFeedback(
            Long feedbackId,
            Long movieId,
            FeedbackRequest request,
            Authentication connectedUser
    ) {



    }
}
