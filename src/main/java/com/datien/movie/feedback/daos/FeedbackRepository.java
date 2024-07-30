package com.datien.movie.feedback.daos;

import com.datien.movie.feedback.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    @Query("""
            SELECT feedback
            FROM Feedback feedback
            WHERE feedback.movie.id = :movieId
            """)
    List<Feedback> findAllFeedbackByMovieId(Long movieId);

    @Query("""
            SELECT feedback
            FROM Feedback feedback
            WHERE feedback.movie.id = :movieId
            AND feedback.id = :feedbackId
           """)
    Feedback findFeedbackByMovieId(Long feedbackId, Long id);
}
