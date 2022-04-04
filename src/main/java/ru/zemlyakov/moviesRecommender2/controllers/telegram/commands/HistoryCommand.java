package ru.zemlyakov.moviesRecommender2.controllers.telegram.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.telegramServices.SendMessageBotService;
import ru.zemlyakov.moviesRecommender2.models.Movie;
import ru.zemlyakov.moviesRecommender2.services.UserService;

import java.util.List;

public class HistoryCommand implements Command {

    private static final String HISTORY_TEXT = "Ниже представлена твоя история просмотра фильмов :) ";
    private final String EMPTY_HISTORY = "Увы, твоя история пуста :(\n" +
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
        messageService.sendMessage(chatId.toString(), getHistory(chatId));
    }

    private String getHistory(long chatId) {
        List<Movie> history = userService.getMovieFromUserHistory(chatId,0);
        StringBuilder sb = new StringBuilder(HISTORY_TEXT).append("\n\n");

        if (history.isEmpty()) {
            sb.append(EMPTY_HISTORY);
        } else {
            for (Movie movie : history)
                sb.append(movie.getTitle()).append("\n");
        }
        return sb.toString();
    }

}
