package com.datien.movie.movie.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieResponse {

    private Long id;
    private String name;
    private int releasedYear;
    private String directorName;
    private String genres;
    private String synopsis;
    private String owner;
    private byte[] movieCover;
    private double rate;
    private long viewedCount;
    private boolean archived;
}
