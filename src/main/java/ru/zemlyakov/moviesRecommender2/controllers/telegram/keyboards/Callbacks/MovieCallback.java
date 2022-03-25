package ru.zemlyakov.moviesRecommender2.controllers.telegram.keyboards.Callbacks;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.keyboards.MovieKeyboard;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.telegramServices.SendMessageBotService;
import ru.zemlyakov.moviesRecommender2.models.Movie;
import ru.zemlyakov.moviesRecommender2.models.User;
import ru.zemlyakov.moviesRecommender2.services.MovieService;
import ru.zemlyakov.moviesRecommender2.services.UserService;

import java.util.regex.Pattern;

public class MovieCallback implements Callback {

    private final SendMessageBotService messageService;
    private final UserService userService;
    private final MovieService movieService;

    public MovieCallback(
            SendMessageBotService messageService,
            UserService userService,
            MovieService movieService
    ) {
        this.messageService = messageService;
        this.userService = userService;
        this.movieService = movieService;
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String[] movieParameters = callbackQuery.getData().split("\\^");
        Long chatId = message.getChatId();
        User user = userService.getUserWithFullInf(chatId);
        Movie movieHasBeenWatched;

        if (Pattern.compile("[а-яА-ЯеЁ]+").matcher(movieParameters[1]).find()) {
             movieHasBeenWatched = movieService.getMovie(
                    movieParameters[1],
                    Short.parseShort(movieParameters[2])
            );
        }
        else {
            movieHasBeenWatched = movieService.getMovieOriginalTitle(
                    movieParameters[1],
                    Short.parseShort(movieParameters[2])
            );
        }

        user.getHistoryOfViewing().add(movieHasBeenWatched);
        userService.saveOrUpdate(user);

        messageService.deleteMessage(
                chatId.toString(),
                message.getMessageId()
                );

        Movie newRecommendMovie =
                movieService.getRecommendation(user, user.getPageOfRecommend()-1, 3).get(2);

        messageService.sendMessage(
                chatId.toString(),
                newRecommendMovie.toRepresent(),
                MovieKeyboard.getListMovieButtons(newRecommendMovie)
        );
    }
}
