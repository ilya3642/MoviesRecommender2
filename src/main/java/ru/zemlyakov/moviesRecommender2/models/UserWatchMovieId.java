package ru.zemlyakov.moviesRecommender2.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserWatchMovieId implements Serializable {

    private static final long serialVersionUID = -6160475877046935930L;
    @Column(
            name = "user_id"
    )
    Long userId;

    @Column(
            name = "movie_id"
    )
    Long movieId;

    public UserWatchMovieId(Long userId, Long movieId) {
        this.userId = userId;
        this.movieId = movieId;
    }

    public UserWatchMovieId() {

    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getMovieId() {
        return movieId;
    }

    public void setMovieId(Long movieId) {
        this.movieId = movieId;
    }
}
