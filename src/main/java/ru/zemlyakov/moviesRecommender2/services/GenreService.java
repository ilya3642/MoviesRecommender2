package ru.zemlyakov.moviesRecommender2.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.zemlyakov.moviesRecommender2.models.Genre;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public boolean existsGenre(String genreName) {
        Optional<Genre> genreOptional = genreRepository.findByGenreName(genreName);
        return genreOptional.isPresent();
    }

    public Genre getGenre(String genreName) {
        Optional<Genre> genreOptional = genreRepository.findByGenreName(genreName);
        if (genreOptional.isEmpty()) {
            throw new IllegalStateException("No genre with this name = " + genreName);
        }
        return genreOptional.get();
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }



    public Genre addNewGenre(Genre newGenre) {

        String lowerName = newGenre.getGenreName().toLowerCase(Locale.ROOT);
        Optional<Genre> genreOptional = genreRepository.findByGenreName(lowerName);
        if (genreOptional.isEmpty()) {
            newGenre.setGenreName(lowerName);
            return genreRepository.save(newGenre);
        }
        else
             return genreOptional.get();
        //        genreRepository.save(newGenre);
    }

    @Transactional
    public void addAllNewGenres(Collection<Genre> newGenres) {
        genreRepository.saveAll(newGenres);
        }

}
