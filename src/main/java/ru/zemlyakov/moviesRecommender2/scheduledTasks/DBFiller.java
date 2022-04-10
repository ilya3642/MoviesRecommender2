package ru.zemlyakov.moviesRecommender2.scheduledTasks;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.zemlyakov.moviesRecommender2.models.Genre;
import ru.zemlyakov.moviesRecommender2.models.Movie;
import ru.zemlyakov.moviesRecommender2.models.Rating;
import ru.zemlyakov.moviesRecommender2.services.GenreService;
import ru.zemlyakov.moviesRecommender2.services.MovieService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@EnableScheduling
public class DBFiller {

    private long movieId;
    private final String kinopoiskAPI;
    private final String keyAPI;
    private final String metacriticURL;

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private HttpEntity<String> request;
    private final MovieService movieService;
    private final GenreService genreService;

    @Autowired
    public DBFiller(
            @Value("${start.movie.id}") long movieId,
            @Value("${kinopoisk.key}") String keyAPI,
            @Value("${kinopoisk.api}") String kinopoiskAPI,
            @Value("${metacritic.url}") String metacriticURL,
            MovieService movieService,
            GenreService genreService
    ) {
        this.movieId = movieId;
        this.kinopoiskAPI = kinopoiskAPI;
        this.keyAPI = keyAPI;
        this.metacriticURL = metacriticURL;
        restTemplate = new RestTemplate();
        mapper = new ObjectMapper();
        this.movieService = movieService;
        this.genreService = genreService;
    }

    public String getKeyAPI() {
        return keyAPI;
    }

    @PostConstruct
    private void buildDBFiller() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("X-API-KEY", keyAPI);
        request = new HttpEntity<>("body", headers);
    }

    @Scheduled(cron = "${cron.expression}")
    public void fillDB() {

        for (int i = 0; i < 500; i++) {
            try {
                JsonNode root = sendKinopoiskRequest();
                movieService.addNewMovie(getMovieFromJSON(root));
            } catch (JsonProcessingException | HttpClientErrorException ex) {
                System.out.println("404, not found movie for id = " + movieId);
            } catch (IllegalStateException ex) {
                System.out.println(ex.getMessage() + " id = " + movieId);
            }
        }
    }

    private JsonNode sendKinopoiskRequest() throws JsonProcessingException {
        ResponseEntity<String> response = restTemplate.exchange(
                kinopoiskAPI + movieId++,
                HttpMethod.GET,
                request,
                String.class
        );
        return mapper.readTree(response.getBody());
    }

    private Movie getMovieFromJSON(JsonNode root) {
        String description = root.get("shortDescription").toString();

        if (description.equals("null")) {
            StringBuilder builder = new StringBuilder(root.get("description").toString());
            if (builder.length() > 507) {
                builder.delete(507, builder.length() - 1);
                builder.append("...");
            }
            description = builder.toString();
        }

        if (description.equals("null")) {
            throw new IllegalStateException("Movie dont have description");
        }

        Float metacriticRating = 0f;

        String originalTitle = root.get("nameOriginal").toString();
        if (!originalTitle.equals("null")) {
            metacriticRating = sendMetacriticRequest(originalTitle);
            if (originalTitle.length() > 53)
                originalTitle = new StringBuilder(originalTitle).delete(51, originalTitle.length() - 1).append("...").toString();
        }


        Rating ratingOfNewMovie = new Rating(
                root.get("ratingKinopoisk").floatValue(),
                root.get("ratingImdb").floatValue(),
                root.get("ratingFilmCritics").floatValue(),
                metacriticRating);

        if ((ratingOfNewMovie.getRatingKinopoisk() == 0 && ratingOfNewMovie.getRatingIMDB() == 0)
                || ratingOfNewMovie.getWeightedAverage() < 3) {
            throw new IllegalStateException("Movie dont have enough userscore");
        }

        String russianTitle = root.get("nameRu").toString();

        if (russianTitle.length() > 24 && (originalTitle.equals("null") || originalTitle.length() > 55))
            throw new IllegalStateException("Movie dont have simple title");

        String webURL = root.get("webUrl").toString();
        Movie newMovie = new Movie(
                russianTitle,
                (short) root.get("year").asInt(),
                description,
                webURL.substring(1, webURL.length() - 1)
        );

        newMovie.setRating(ratingOfNewMovie);

        if (!originalTitle.equals("null")) {
            newMovie.setOriginalTitle(originalTitle);
        }

        List<String> newGenres = root.get("genres").findValuesAsText("genre");
        Set<Genre> genresOfMovie = new HashSet<>();

        newGenres.stream()
                .filter(s -> s.length() != 0)
                .map(String::toLowerCase)
                .map(Genre::new)
                .forEach(genre ->
                        genresOfMovie.add(
                                genreService.getOrSave(genre)
                        )
                );

        newMovie.setGenre(genresOfMovie);
        String posterURL = root.get("posterUrlPreview").toString();
        if (!posterURL.equals("null")) {
            newMovie.setPosterURL(posterURL.substring(1, posterURL.length() - 1));
        }

        return newMovie;
    }

    private Float sendMetacriticRequest(String title) {
        String formatTitle = getTitleForRequest(title);
        float metacriticScore = 0f;

        try {
            Document document = Jsoup
                    .connect(String.format(metacriticURL, formatTitle))
                    .userAgent("Chrome/79.0.3945.117")
                    .get();

            metacriticScore = Float.parseFloat(document.getElementsByClass("num_wrapper").get(0).text()) / 10.0f;
        } catch (IOException | NumberFormatException | IndexOutOfBoundsException e) {
            System.out.println("Movie with id = " + movieId + " dont have metacritic score!");
        }

        return metacriticScore;
    }

    private String getTitleForRequest(String title) {
        title = title.substring(1, title.length() - 1)
                .replaceAll("(')|(\\.)", "");
        title = title
                .replaceAll("\\W", " ")
                .replaceAll("\\s+", " ")
                .toLowerCase();
        StringBuilder builder = new StringBuilder();

        for (String element : title.split(" ")) {
            builder.append(element);
            builder.append("-");
        }

        builder.delete(builder.length() - 1, builder.length());
        return builder.toString();
    }

}
