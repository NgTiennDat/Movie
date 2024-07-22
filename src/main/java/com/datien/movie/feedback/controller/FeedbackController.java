package com.datien.movie.feedback.controller;

import com.datien.movie.feedback.model.FeedbackRequest;
import com.datien.movie.feedback.model.FeedbackResponse;
import com.datien.movie.feedback.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/feedbacks")
    public ResponseEntity<Long> createFeedback(
            @RequestBody @Valid FeedbackRequest request,
            Authentication connectedUser
    ) {
        Long feedbackId = feedbackService.addFeedback(request, connectedUser);
        return ResponseEntity.ok(feedbackId);
    }

    @GetMapping("/feedbacks/{movie-id}")
    public ResponseEntity<List<FeedbackResponse>> getAllFeedbacksOfMovie(
            @PathVariable("movie-id") Long movieId
    ) {
        return ResponseEntity.ok(feedbackService.getAllFeedbacksOfMovie(movieId));
    }
}
