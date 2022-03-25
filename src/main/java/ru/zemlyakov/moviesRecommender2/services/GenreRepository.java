package ru.zemlyakov.moviesRecommender2.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.zemlyakov.moviesRecommender2.models.Genre;

import javax.persistence.NamedQuery;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre,Long> {

    @Query("SELECT g FROM Genre g WHERE g.genreName=?1")
    Optional<Genre> findByGenreName(String genreName);

}
