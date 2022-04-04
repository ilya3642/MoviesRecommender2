package ru.zemlyakov.moviesRecommender2.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Table(
        name = "MR_MOVIES",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "unique_movie_name",
                        columnNames = {"title", "original_title", "year_of_create"}
                )
        }
)
public class Movie {

    @Id
    @SequenceGenerator(
            name = "sequence_movie_id",
            sequenceName = "sequnce_movie_id",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "sequence_movie_id",
            strategy = SEQUENCE)
    @Column(
            name = "movie_id"
    )
    private long movieId;

    @Column(
            length = 128
    )
    @NotBlank
    private String title;

    @Column(
            name = "original_title",
            length = 128
    )
    @NotBlank
    private String originalTitle;

    @Column(
            nullable = false
    )
    @NotBlank
    private String webURL;

    @ManyToMany(
            cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH},
            fetch = FetchType.LAZY
    )
    @JoinTable(
            name = "MR_Genres_of_movie",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genre;

    @Column(
            name = "year_of_create",
            nullable = false,
            columnDefinition = "smallint"
    )
    @Size(min = 1900, max = 2022)
    private short yearOfCreate;


    @Column(
            nullable = false,
            length = 511
    )
    @NotBlank
    private String description;


    @Embedded
    private Rating rating;

    @OneToMany(
            mappedBy = "movie",
            fetch = FetchType.LAZY
    )
    private Set<UserWatchMovie> usersSeenThisFilm;

    @NotBlank
    @Column
    private String posterURL;

    public Movie(long movieId,
                 String title,
                 short yearOfCreate,
                 String description,
                 String originalTitle,
                 String webURL,
                 String posterURL,
                 Set<Genre> genre,
                 Rating rating) {
        this.movieId = movieId;
        this.title = title;
        this.yearOfCreate = yearOfCreate;
        this.description = description;
        this.originalTitle = originalTitle;
        this.webURL = webURL;
        this.posterURL = posterURL;
        this.genre = genre;
        this.rating = rating;
    }

    public Movie(String title, short yearOfCreate, String description, String webURL) {
        this.title = title;
        this.yearOfCreate = yearOfCreate;
        this.description = description;
        this.webURL = webURL;
    }


    public Movie() {
    }

    public long getMovieId() {
        return movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<Genre> getGenre() {
        return genre;
    }

    public void setGenre(Set<Genre> genre) {
        this.genre = genre;
    }

    public short getYearOfCreate() {
        return yearOfCreate;
    }

    public void setYearOfCreate(short yearOfCreate) {
        this.yearOfCreate = yearOfCreate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getWebURL() {
        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }

    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    @Override
    public String toString() {
        return title +
                ", " + yearOfCreate +
                ", " + description +
                ", " + rating;
    }

    public String toRepresent() {
        StringBuilder builder = new StringBuilder(512);

        for (Genre genre : genre)
            builder.append(genre.getGenreName()).append(", ");

        builder.delete(builder.length() - 2, builder.length());
        builder.append("[.](").append(posterURL).append(")");

        String represent = String.format("* %s, %d*\n" +
                        " " + builder + " \n\n" +
                        "_%s_\n\n" +
                        "*Бот %.2f*   КП %.2f   IMDB %.2f",
                title,
                yearOfCreate,
                description,
                rating.getWeightedAverage(),
                rating.getRatingKinopoisk(),
                rating.getRatingIMDB()
        );

        String tomatoes = "   RotTom ";
        String metacritic = "   Meta ";

        if (rating.getRatingRottenTomatoes() == 0)
            tomatoes += "-- ";
        else tomatoes += String.valueOf(rating.getRatingRottenTomatoes());

        if (rating.getRatingMetacritic() == 0)
            metacritic += "-- ";
        else metacritic += String.valueOf(rating.getRatingMetacritic());

        return represent + tomatoes + metacritic;

    }

    public String toShortRepresent() {

        return String.format("\n*%s, %d*" +
                        "[.](" + posterURL + ")\n\n" +
                        "_%s_",
                title,
                yearOfCreate,
                description
        );

    }

}


