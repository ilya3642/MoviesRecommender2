package ru.zemlyakov.moviesRecommender2.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.zemlyakov.moviesRecommender2.models.Movie;
import ru.zemlyakov.moviesRecommender2.models.User;

import javax.validation.constraints.NotNull;
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

    Page<Movie> findDistinctByIdNotInAndYearOfCreateBetween(
            Collection<Long> movieId,
            short minYear,
            short maxYear,
            Pageable pageable
    );

    Page<Movie> findDistinctByIdNotInAndGenreIdInAndYearOfCreateBetween(
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

    @Query("SELECT m FROM Movie m JOIN FETCH Genre g WHERE m.id IN ?1")
    Page<Movie> findAllByGenreIdIn(
            Collection<Long> genreId, Pageable pageable
    );


    @Query("SELECT m FROM Movie m WHERE NOT EXISTS (SELECT 1 FROM User u WHERE u=?1 and m member of u.historyOfViewing) AND" +
            " m.rating.weightedAverage > ?2")
    Page<Movie> findByRatingIsGreaterThanAndByHistoryOfUserOrderByRatingDesc(
            @NotNull User ID,
            float ratingLimit,
            Pageable pageable
    );

}
