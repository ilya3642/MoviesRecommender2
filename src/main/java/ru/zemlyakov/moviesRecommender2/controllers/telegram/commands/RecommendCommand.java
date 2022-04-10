package ru.zemlyakov.moviesRecommender2.controllers.telegram.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.keyboards.MovieKeyboard;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.telegramServices.SendMessageBotService;
import ru.zemlyakov.moviesRecommender2.models.Movie;
import ru.zemlyakov.moviesRecommender2.models.User;
import ru.zemlyakov.moviesRecommender2.services.MovieService;
import ru.zemlyakov.moviesRecommender2.services.UserService;

import java.util.List;

public class RecommendCommand implements Command {

    private final SendMessageBotService messageService;
    private final UserService userService;
    private final MovieService movieService;

    private static final String RECOMMEND_TEXT = "Вот подборка фильмов, " +
            "которые могут тебя заинтересовать";
    private static final String NOT_FOUND_TEXT = "Похоже, что мне нечего тебе предложить :( " +
            "Попробуй изменить параметры поиска: жанр или год. В следующий раз я постараюсь поискать получше и обязательно что-нибудь для тебя найду";

    public RecommendCommand(SendMessageBotService messageService,
                            UserService userService,
                            MovieService movieService) {
        this.messageService = messageService;
        this.userService = userService;
        this.movieService = movieService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        User user = userService.getUserWithHistory(chatId);
        messageService.sendMessage(chatId.toString(), RECOMMEND_TEXT);

        List<Movie> recommendation = movieService.getRecommendation(user, user.getDeepOfRecommend(), 3);
        userService.updateUserRecommendDeep(user);

        if (recommendation.isEmpty()) {
            messageService.sendMessage(
                    chatId.toString(),
                    NOT_FOUND_TEXT
            );
        } else for (Movie movie : recommendation) {
            messageService.sendMessage(
                    chatId.toString(),
                    movie.toRepresent(),
                    MovieKeyboard.getListMovieButtons(movie)
            );
        }
    }

}
