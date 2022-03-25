package ru.zemlyakov.moviesRecommender2.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "mr_users")
public class User {

    @SequenceGenerator(
            name = "sequence_user_id",
            sequenceName = "sequence_user_id",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "sequence_user_id",
            strategy = GenerationType.SEQUENCE
    )
    @Id
    @Column(name = "user_id")
    private long id;

    @Column(
            name = "chat_id",
            nullable = false,
            unique = true
    )
    private long chatId;

    @Column(
            name = "user_name",
            length = 64,
            nullable = false
    )
    @NotBlank
    private String userName;

    @Column(
            name = "min_year_movie",
            length = 4,
            nullable = false,
            columnDefinition = "smallint"
    )
    @Size(min = 1900, max = 2022)
    private short minYearOfCreateMovie = 1900;

    @Column(
            name = "max_year_movie",
            length = 4,
            nullable = false,
            columnDefinition = "smallint"
    )
    @Size(min = 1900, max = 2022)
    private short maxYearOfCreateMovie = (short) LocalDate.now().getYear();

    @Column
    int pageOfRecommend = 0;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}
    )
    @JoinTable(
            name = "MR_User_seen_movie",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id")
    )
    private Set<Movie> historyOfViewing;

    @ManyToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH}
    )
    @JoinTable(
            name = "MR_User_favourite_genre",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> favouriteGenres;

    public User(long id, long chatId, String userName, short minYearOfCreateMovie, short maxYearOfCreateMovie) {
        this.id = id;
        this.chatId = chatId;
        this.userName = userName;
        this.minYearOfCreateMovie = minYearOfCreateMovie;
        this.maxYearOfCreateMovie = maxYearOfCreateMovie;
    }

    public User(long chatId, String userName, short minYearOfCreateMovie, short maxYearOfCreateMovie) {
        this.chatId = chatId;
        this.userName = userName;
        this.minYearOfCreateMovie = minYearOfCreateMovie;
        this.maxYearOfCreateMovie = maxYearOfCreateMovie;
    }

    public User(long chatId, String userName) {
        this.chatId = chatId;
        this.userName = userName;
    }

    public User() {
    }

    public long getId() {
        return id;
    }

    public long getChatId() {
        return chatId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Set<Movie> getHistoryOfViewing() {
        return historyOfViewing;
    }

    public void setHistoryOfViewing(Set<Movie> historyOfViewing) {
        this.historyOfViewing = historyOfViewing;
    }

    public Set<Genre> getFavouriteGenres() {
        return favouriteGenres;
    }

    public void setFavouriteGenres(Set<Genre> favouriteGenres) {
        this.favouriteGenres = favouriteGenres;
    }

    public short getMinYearOfCreateMovie() {
        return minYearOfCreateMovie;
    }

    public void setMinYearOfCreateMovie(short minYearOfCreateMovie) {
        this.minYearOfCreateMovie = minYearOfCreateMovie;
    }

    public short getMaxYearOfCreateMovie() {
        return maxYearOfCreateMovie;
    }

    public void setMaxYearOfCreateMovie(short maxYearOfCreateMovie) {
        this.maxYearOfCreateMovie = maxYearOfCreateMovie;
    }

    public int getPageOfRecommend() {
        return pageOfRecommend++;
    }

    public void refreshPageOfRecommend() {
        pageOfRecommend = 0;
    }

}
