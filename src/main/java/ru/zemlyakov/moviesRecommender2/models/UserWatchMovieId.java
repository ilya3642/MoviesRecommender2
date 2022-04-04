package ru.zemlyakov.moviesRecommender2.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserWatchMovieId that = (UserWatchMovieId) o;
        return userId.equals(that.userId) && movieId.equals(that.movieId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, movieId);
    }
}
