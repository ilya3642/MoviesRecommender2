package ru.zemlyakov.moviesRecommender2.controllers.telegram.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.keyboards.HistoryKeyboard;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.telegramServices.SendMessageBotService;
import ru.zemlyakov.moviesRecommender2.models.Movie;
import ru.zemlyakov.moviesRecommender2.models.User;
import ru.zemlyakov.moviesRecommender2.services.UserService;

import java.util.List;

public class HistoryCommand implements Command {

    private static final String HISTORY_TEXT = "Ниже представлена твоя история просмотра фильмов :)\n\n";
    private static final String EMPTY_HISTORY = "Увы, твоя история пуста :(\n" +
            "Но ты можешь это исправить! Для того чтобы получить рекомендацию используй комманду /recommend";

    private final SendMessageBotService messageService;
    private final UserService userService;

    public HistoryCommand(SendMessageBotService messageService,
                          UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        User user = userService.getUser(chatId);
        List<Movie> history = userService.getMovieFromUserHistory(user,0);
        int historySize = userService.getHistorySize(user);

        if (history.isEmpty()) {
            messageService.sendMessage(chatId.toString(), EMPTY_HISTORY);
        } else {
            Movie movie = history.get(0);
            messageService.sendMessage(
                    chatId.toString(),
                    HISTORY_TEXT + movie.toShortRepresent(),
                    HistoryKeyboard.getListHistoryButtons(movie, 1, historySize)
            );
        }
    }

}
