package com.datien.movie.movie.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record MovieRequest(

        @NotNull(message = "100")
        @NotEmpty(message = "100")
        Long id,
        @NotNull(message = "101")
        @NotEmpty(message = "101")
        String name,
        @NotNull(message = "102")
        @NotEmpty(message = "102")
        int releasedYear,
        @NotNull(message = "103")
        @NotEmpty(message = "103")
        String directorName,
        @NotNull(message = "104")
        @NotEmpty(message = "104")
        String isbn,
        @NotNull(message = "105")
        @NotEmpty(message = "105")
        String synopsis
) {
}
