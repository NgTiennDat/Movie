package com.datien.movie.movie.repo;

import com.datien.movie.movie.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

    @Query("""
            SELECT movie
            FROM Movie movie
            WHERE movie.archived = false
            AND movie.owner.id != :userId
            """)
    Page<Movie> findDisplayableMovies(Pageable pageable, Long userId);

    @Query("""
        SELECT movie
        FROM Movie movie
        WHERE movie.genres LIKE %:genre%
        """)
    List<Movie> findByGenres(@Param("genre") String genre);
    @Query("""
            SELECT movie
            FROM Movie movie
            WHERE movie.genres LIKE %:genre%
            """)
    Page<Movie> findByGenresPaging(Pageable pageable, String genre);

    @Query("""
            SELECT movie
            FROM Movie movie
            ORDER BY movie.id DESC
            """)
    List<Movie> findByTopRating();
}
