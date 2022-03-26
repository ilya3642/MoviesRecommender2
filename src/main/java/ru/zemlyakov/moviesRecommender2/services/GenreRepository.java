package ru.zemlyakov.moviesRecommender2.services;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.QueryHints;
import ru.zemlyakov.moviesRecommender2.models.Genre;

import javax.persistence.QueryHint;
import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre,Long> {

    @Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
    @QueryHints(value = {@QueryHint(name = org.hibernate.jpa.QueryHints.HINT_CACHEABLE, value = "true")})
    Optional<Genre> findByGenreName(String genreName);

}
