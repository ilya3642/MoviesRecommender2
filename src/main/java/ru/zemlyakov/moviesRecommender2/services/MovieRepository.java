package ru.zemlyakov.moviesRecommender2.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zemlyakov.moviesRecommender2.models.Movie;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByTitleAndOriginalTitleAndYearOfCreate(
            String title,
            String originalTitle,
            short yearOfCreate
    );

    Optional<Movie> findByTitleAndYearOfCreate(
            String title,
            short yearOfCreate
    );

    Optional<Movie> findByOriginalTitleAndYearOfCreate(
            String title,
            short yearOfCreate
    );

    Page<Movie> findDistinctByYearOfCreateBetween(
            short minYear,
            short maxYear,
            Pageable pageable
    );

    Page<Movie> findDistinctByMovieIdNotInAndYearOfCreateBetween(
            Collection<Long> movieId,
            short minYear,
            short maxYear,
            Pageable pageable
    );

    Page<Movie> findDistinctByMovieIdNotInAndGenreIdInAndYearOfCreateBetween(
            Collection<Long> movieId,
            Collection<Long> genreId,
            short minYear,
            short maxYear,
            Pageable pageable
    );

    Page<Movie> findDistinctByGenreIdInAndYearOfCreateBetween(
            Collection<Long> genreId,
            short minYear,
            short maxYear,
            Pageable pageable
    );

}
