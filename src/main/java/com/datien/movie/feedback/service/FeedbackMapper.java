package com.datien.movie.feedback.service;

import com.datien.movie.feedback.model.Feedback;
import com.datien.movie.feedback.model.FeedbackRequest;
import com.datien.movie.feedback.model.FeedbackResponse;
import com.datien.movie.movie.model.Movie;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class FeedbackMapper {

    public Feedback toFeedback(FeedbackRequest request) {
        return Feedback.builder()
                .note(request.note())
                .comment(request.comment())
                .movie(Movie.builder()
                        .id(request.movieId())
                        .archived(false)
                        .build())
                .build();

    }

    public FeedbackResponse toFeedbackResponse(Feedback feedback, Long id) {
        return FeedbackResponse.builder()
                .note(feedback.getNote())
                .comment(feedback.getComment())
                .ownFeedback(Objects.equals(feedback.getCreatedBy(), id))
                .build();
    }
}
