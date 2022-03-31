package ru.zemlyakov.moviesRecommender2.models;

import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.NaturalIdCache;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.GenerationType.SEQUENCE;
import static org.hibernate.annotations.CacheConcurrencyStrategy.READ_ONLY;

@Entity
@Table(name = "MR_GENRES")
@Cacheable
@org.hibernate.annotations.Cache(
        usage = READ_ONLY
)
@NaturalIdCache
@SelectBeforeUpdate
public class Genre {

    @SequenceGenerator(
            name = "sequence_genre_id",
            sequenceName = "sequence_genre_id",
            allocationSize = 1)
    @GeneratedValue(
            generator = "sequence_genre_id",
            strategy = SEQUENCE)
    @Id
    @Column(
            name = "genre_id"
    )
    private long id;

    @ManyToMany(
            mappedBy = "genre",
            fetch = FetchType.LAZY
    )
    private Set<Movie> movieOfThisGenre;

    @Column(
            nullable = false,
            length = 64,
            unique = true
    )
    @NotBlank
    @NaturalId
    private String genreName;

    @ManyToMany(
            mappedBy = "favoriteGenres",
            fetch = FetchType.LAZY
    )
    private Set<User> usersLikeGenre;

    public Genre(long id, String genreName) {
        this.id = id;
        this.genreName = genreName;
    }

    public Genre(String genreName) {
        this.genreName = genreName;
    }

    public Genre() {
    }

    public long getId() {
        return id;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "genreName='" + genreName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Genre genre = (Genre) o;
        return genreName.equals(genre.genreName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreName);
    }
}
