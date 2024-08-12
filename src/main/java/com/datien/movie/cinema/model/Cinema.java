package com.datien.movie.cinema.model;

import com.datien.movie.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tbl_cinema")
public class Cinema extends BaseEntity {

    @Column(name = "CINEMA_NAME")
    private String cinemaName;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "NUMBER_OF_SEAT_REMAIN")
    private int numberOfSeats;

    @Column(name = "CONTACT_NUMBER")
    private String contactNumber;

}
