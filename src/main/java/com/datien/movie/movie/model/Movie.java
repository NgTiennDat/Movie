package com.datien.movie.movie.model;

import com.datien.movie.common.BaseEntity;
import com.datien.movie.feedback.model.Feedback;
import com.datien.movie.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Movie extends BaseEntity {

    private String name;
    private int releasedYear;
    private String directorName;
    private String genres;
    private String synopsis;
    private String movieCover;
    private long viewCount = 0;
    private boolean archived;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "movie")
    private List<Feedback> feedbacks;

    @Transient
    public double getRate() {
        if(feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }

        var rate = this.feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);

        // Return 4.0 if the rate is less than 4.5, otherwise return 5.0
        return Math.round(rate * 10.0) / 10.0;
    }
}
