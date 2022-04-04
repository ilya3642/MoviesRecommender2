package ru.zemlyakov.moviesRecommender2.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mr_user_watch_movie")
public class UserWatchMovie {

    @EmbeddedId
    UserWatchMovieId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @MapsId("movieId")
    @JoinColumn(name = "movie_id")
    Movie movie;

    LocalDateTime dateTimeToWatched;

    public UserWatchMovie(User user, Movie movie, LocalDateTime dateTimeToWatched) {
        this.id = new UserWatchMovieId(user.getUserId(), movie.getMovieId());
        this.user = user;
        this.movie = movie;
        this.dateTimeToWatched = dateTimeToWatched;
    }

    public UserWatchMovie() {

    }

    public Movie getMovie() {
        return movie;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getDateTimeToWatched() {
        return dateTimeToWatched;
    }
}
