package ru.zemlyakov.moviesRecommender2.models;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE;

@Entity
@Table(name = "mr_users")
@Cacheable
@org.hibernate.annotations.Cache(
        usage = READ_WRITE
)
@NaturalIdCache
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
    @Column(
            name = "user_id"
    )
    private long userId;

    @Column(
            name = "chat_id",
            nullable = false,
            unique = true,
            updatable = false
    )
    @NaturalId
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
    int deepOfRecommend = 0;

    @OneToMany(
            fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            mappedBy = "user"
    )
    private Set<UserWatchMovie> historyOfViewing;

    @ManyToMany(
            cascade = {CascadeType.MERGE, CascadeType.REFRESH},
            fetch = FetchType.EAGER
    )
    @JoinTable(
            name = "MR_User_favourite_genre",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @org.hibernate.annotations.Cache(usage = READ_WRITE)
    private Set<Genre> favoriteGenres;

    public User(long userId, long chatId, String userName, short minYearOfCreateMovie, short maxYearOfCreateMovie) {
        this.userId = userId;
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

    public long getUserId() {
        return userId;
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

    public Set<UserWatchMovie> getHistoryOfViewing() {
        return historyOfViewing;
    }

    public void setHistoryOfViewing(Set<UserWatchMovie> historyOfViewing) {
        this.historyOfViewing = historyOfViewing;
    }

    public Set<Genre> getFavoriteGenres() {
        return favoriteGenres;
    }

    public void setFavoriteGenres(Set<Genre> favouriteGenres) {
        this.favoriteGenres = favouriteGenres;
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

    public int getDeepOfRecommend() {
        return deepOfRecommend++;
    }

    public void refreshPageOfRecommend() {
        deepOfRecommend = 0;
    }

}
