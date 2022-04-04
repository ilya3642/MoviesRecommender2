package ru.zemlyakov.moviesRecommender2.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.zemlyakov.moviesRecommender2.models.Movie;
import ru.zemlyakov.moviesRecommender2.models.User;
import ru.zemlyakov.moviesRecommender2.models.UserWatchMovie;
import ru.zemlyakov.moviesRecommender2.models.UserWatchMovieId;

public interface UserWatchMovieRepository extends JpaRepository<UserWatchMovie, UserWatchMovieId> {

    @Query("SELECT um.movie FROM UserWatchMovie um WHERE um.user=?1")
    Page<Movie> getMovieFromUserHistory(
            User user,
            Pageable pageable
    );

}
