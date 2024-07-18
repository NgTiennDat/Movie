package com.datien.movie.movie.daos;

import com.datien.movie.movie.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

    @Query("""
            SELECT movie
            FROM Movie movie
            WHERE movie.archived = false
            AND movie.owner.id != :userId
            """)
    Page<Movie> findDisplayableMovies(Pageable pageable, Long userId);
}
