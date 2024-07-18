package com.datien.movie.movie.model;

import com.datien.movie.common.BaseEntity;
import com.datien.movie.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Movie extends BaseEntity {

    private String name;
    private int releasedYear;
    private String directorName;
    private String isbn;
    private String synopsis;
    private String movieCover;
    private long viewCount;
    private boolean archived;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "owner_id")
    private User owner;
}
