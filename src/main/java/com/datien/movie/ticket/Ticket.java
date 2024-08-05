package com.datien.movie.ticket;

import com.datien.movie.common.BaseEntity;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ticket extends BaseEntity {
    private String title;

}
