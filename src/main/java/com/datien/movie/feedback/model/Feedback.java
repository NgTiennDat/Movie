package com.datien.movie.feedback.model;

import com.datien.movie.common.BaseEntity;
import com.datien.movie.movie.model.Movie;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Feedback extends BaseEntity {

    private Double note; // rate from 1 to 5 stars
    private String comment; // content of feedback such as "amazing movie and it is worth to spend time watching"

    @ManyToOne
    @JoinColumn(
            name = "movie_id"
    )
    private Movie movie;
}
