package ru.zemlyakov.moviesRecommender2.services;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.zemlyakov.moviesRecommender2.models.Genre;
import ru.zemlyakov.moviesRecommender2.models.Movie;
import ru.zemlyakov.moviesRecommender2.models.Rating;
import ru.zemlyakov.moviesRecommender2.models.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    @Transactional
    public void addNewMovie(Movie newMovie) {
        Optional<Movie> movieOptional = movieRepository
                .findByTitleAndOriginalTitleAndYearOfCreate(
                        newMovie.getTitle(),
                        newMovie.getOriginalTitle(),
                        newMovie.getYearOfCreate());

        if (movieOptional.isPresent()) {
            updateMovie(
                    movieOptional.get().getId(),
                    newMovie.getTitle(),
                    newMovie.getYearOfCreate(),
                    newMovie.getDescription(),
                    newMovie.getOriginalTitle(),
                    newMovie.getWebURL(),
                    newMovie.getGenre(),
                    newMovie.getRating()
            );
        } else {
            movieRepository.save(newMovie);
        }
    }

    @Transactional
    public List<Movie> getRecommendation(User user, int numPage, int limit) {
        PageRequest pageRequest = PageRequest.of(numPage, limit, Sort.by("rating.weightedAverage").descending());
        List<Movie> result;

        if (!user.getHistoryOfViewing().isEmpty() && !user.getFavouriteGenres().isEmpty()) {
            result = movieRepository.findDistinctByIdNotInAndGenreIdInAndYearOfCreateBetween(
                    user.getHistoryOfViewing().stream()
                            .map(Movie::getId)
                            .collect(Collectors.toList()),
                    user.getFavouriteGenres().stream()
                            .map(Genre::getId)
                            .collect(Collectors.toList()),
                    user.getMinYearOfCreateMovie(),
                    user.getMaxYearOfCreateMovie(),
                    pageRequest
            ).getContent();
        } else if (user.getHistoryOfViewing().isEmpty() && !user.getFavouriteGenres().isEmpty()) {
            result = movieRepository.findDistinctByGenreIdInAndYearOfCreateBetween(
                    user.getFavouriteGenres().stream()
                            .map(Genre::getId)
                            .collect(Collectors.toList()),
                    user.getMinYearOfCreateMovie(),
                    user.getMaxYearOfCreateMovie(),
                    pageRequest
            ).getContent();
        } else if (!user.getHistoryOfViewing().isEmpty() && user.getFavouriteGenres().isEmpty()) {
                result = movieRepository.findDistinctByIdNotInAndYearOfCreateBetween(
                        user.getHistoryOfViewing().stream()
                                .map(Movie::getId)
                                .collect(Collectors.toList()),
                        user.getMinYearOfCreateMovie(),
                        user.getMaxYearOfCreateMovie(),
                        pageRequest
                ).getContent();
        } else
            result = movieRepository.findDistinctByYearOfCreateBetween(
                    user.getMinYearOfCreateMovie(),
                    user.getMaxYearOfCreateMovie(),
                    pageRequest
            ).getContent();

            for (Movie movie : result)
                Hibernate.initialize(movie.getGenre());

            return result;
    }

    public Movie getMovie(Long id) {
        Optional<Movie> optionalMovie = movieRepository.findById(id);
        if (optionalMovie.isEmpty()) {
            throw new IllegalStateException("No movie with this id = " + id);
        }
        return optionalMovie.get();
    }

    public Movie getMovie(String title, short yearOfCreate) {
        Optional<Movie> optionalMovie =
                movieRepository.findByTitleAndYearOfCreate(
                        title, yearOfCreate
                );

        if (optionalMovie.isEmpty()) {
            throw new IllegalStateException("No movie with this parameters: title = " +
                    title + ", yearOfCreate = " + yearOfCreate);
        }
        return optionalMovie.get();
    }

    public Movie getMovieOriginalTitle(String originalTitle, short yearOfCreate){
        Optional<Movie> optionalMovie =
                movieRepository.findByOriginalTitleAndYearOfCreate(
                        originalTitle, yearOfCreate
                );

        if (optionalMovie.isEmpty()) {
            throw new IllegalStateException("No movie with this parameters: originalTitle = " +
                    originalTitle + ", yearOfCreate = " + yearOfCreate);
        }
        return optionalMovie.get();
    }

    public void deleteMovie(Long id) {
        boolean exists = movieRepository.existsById(id);
        if (!exists)
            throw new IllegalStateException("Movie with id = " + id + " does not exists!");
        movieRepository.deleteById(id);
    }

    @Transactional
    public void updateMovie(Long id,
                            String title,
                            Short yearOfCreate,
                            String description,
                            String originalTitle,
                            String webURL,
                            Set<Genre> genre,
                            Rating rating) {
        Movie updatableMovie = movieRepository
                .findById(id)
                .orElseThrow(() -> new IllegalStateException("Movie with id = " + id + "does not exists"));

        if (title != null &&
                title.length() > 3 &&
                !Objects.equals(title, updatableMovie.getTitle())) {
            updatableMovie.setTitle(title);
        }

        if (yearOfCreate != null &&
                yearOfCreate > 1900 &&
                yearOfCreate != updatableMovie.getYearOfCreate()) {
            updatableMovie.setYearOfCreate(yearOfCreate);
        }

        if (description != null &&
                description.length() > 3 &&
                !Objects.equals(description, updatableMovie.getDescription())) {
            updatableMovie.setDescription(description);
        }

        if (originalTitle != null &&
                originalTitle.length() > 3 &&
                !Objects.equals(originalTitle, updatableMovie.getOriginalTitle())) {
            updatableMovie.setOriginalTitle(originalTitle);
        }

        if (webURL != null &&
                webURL.length() > 3 &&
                !Objects.equals(webURL, updatableMovie.getWebURL())) {
            updatableMovie.setWebURL(webURL);
        }

        if (genre != null &&
                !Objects.equals(genre, updatableMovie.getGenre())) {
            updatableMovie.setGenre(genre);
        }

        if (rating != null &&
                !Objects.equals(rating, updatableMovie.getRating())) {
            updatableMovie.setRating(rating);
        }

    }
}