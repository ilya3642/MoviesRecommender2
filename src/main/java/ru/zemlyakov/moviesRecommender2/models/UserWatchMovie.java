package ru.zemlyakov.moviesRecommender2.models;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
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

    Timestamp dateTimeToWatched;

    public UserWatchMovie(User user, Movie movie, Timestamp dateTimeToWatched) {
        this.user = user;
        this.movie = movie;
        this.dateTimeToWatched = dateTimeToWatched;
    }

    public UserWatchMovie() {

    }
}
