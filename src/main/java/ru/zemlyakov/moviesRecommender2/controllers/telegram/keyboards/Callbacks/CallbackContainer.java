package ru.zemlyakov.moviesRecommender2.controllers.telegram.keyboards.Callbacks;

import com.google.common.collect.ImmutableMap;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.telegramServices.SendMessageBotService;
import ru.zemlyakov.moviesRecommender2.services.GenreService;
import ru.zemlyakov.moviesRecommender2.services.MovieService;
import ru.zemlyakov.moviesRecommender2.services.UserService;

import static ru.zemlyakov.moviesRecommender2.controllers.telegram.keyboards.Callbacks.CallbackName.*;

public class CallbackContainer {

    private final ImmutableMap<String, Callback> callbackMap;

    public CallbackContainer(
            SendMessageBotService sendMessageBotService,
            UserService userService,
            MovieService movieService,
            GenreService genreService) {

        callbackMap = ImmutableMap.<String, Callback>builder()
                .put(GENRE.getCallbackName(), new GenreCallback(sendMessageBotService, userService, genreService))
                .put(MOVIE.getCallbackName(), new MovieCallback(sendMessageBotService, userService, movieService))
//                .put(HISTORY.getCallbackName(), new HistoryCallback(sendMessageBotService, userService, genreService))
                .build();

    }

    public Callback extractHandler(String handlerIdentifier) {
        return callbackMap.get(handlerIdentifier);
    }

}
