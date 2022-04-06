package ru.zemlyakov.moviesRecommender2.controllers.telegram.keyboards.Callbacks;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.keyboards.HistoryKeyboard;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.telegramServices.SendMessageBotService;
import ru.zemlyakov.moviesRecommender2.models.Movie;
import ru.zemlyakov.moviesRecommender2.models.User;
import ru.zemlyakov.moviesRecommender2.services.MovieService;
import ru.zemlyakov.moviesRecommender2.services.UserService;

import java.util.List;
import java.util.regex.Pattern;

public class HistoryCallback implements Callback {

    private static final String HISTORY_TEXT = "Ниже представлена твоя история просмотра фильмов :)\n";
    private static final String EMPTY_HISTORY = "Увы, твоя история пуста :(\n" +
            "Но ты можешь это исправить! Для того чтобы получить рекомендацию используй комманду /recommend";

    private final SendMessageBotService messageService;
    private final UserService userService;
    private final MovieService movieService;

    public HistoryCallback(SendMessageBotService messageService,
                           UserService userService,
                           MovieService movieService) {
        this.messageService = messageService;
        this.userService = userService;
        this.movieService = movieService;
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {

        Message message = callbackQuery.getMessage();
        String[] historyParameters = callbackQuery.getData().split("\\^");
        Long chatId = message.getChatId();
        boolean needToRefreshNumPage = false;
        int numOfPage = 1;

        if (historyParameters.length > 2) {
            handleMovie(historyParameters, userService.getUserWithFullInf(chatId));
            needToRefreshNumPage = true;
        } else if ("close".equals(historyParameters[1])) {
            messageService.deleteMessage(
                    chatId.toString(),
                    message.getMessageId()
            );
            return;
        } else if (Integer.parseInt(historyParameters[1]) == -1) {
            return;
        }

        if (!needToRefreshNumPage) {
            numOfPage = Integer.parseInt(historyParameters[1]);
        }

        User user = userService.getUser(chatId);

        messageService.deleteMessage(
                chatId.toString(),
                message.getMessageId()
        );

        int historySize = userService.getHistorySize(user);

        if (historySize == 0) {
            messageService.sendMessage(
                    chatId.toString(),
                    EMPTY_HISTORY
            );
        } else {
            List<Movie> history = userService.getMovieFromUserHistory(user, numOfPage - 1);
            Movie movie = history.get(0);

            messageService.sendMessage(
                    chatId.toString(),
                    HISTORY_TEXT + movie.toShortRepresent(),
                    HistoryKeyboard.getListHistoryButtons(movie, numOfPage, historySize)
            );
        }
    }


    void handleMovie(String[] movieParameters, User user) {
        Movie movieShouldBeDeletedFromHistory;

        if (Pattern.compile("[а-яА-ЯеЁ]+").matcher(movieParameters[1]).find()) {
            movieShouldBeDeletedFromHistory = movieService.getMovie(
                    movieParameters[1],
                    Short.parseShort(movieParameters[2])
            );
        } else {
            movieShouldBeDeletedFromHistory = movieService.getMovieOriginalTitle(
                    movieParameters[1],
                    Short.parseShort(movieParameters[2])
            );
        }

        userService.deleteMovieFromHistory(user, movieShouldBeDeletedFromHistory);

    }
}
