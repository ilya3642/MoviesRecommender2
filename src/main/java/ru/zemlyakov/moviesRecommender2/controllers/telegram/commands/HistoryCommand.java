package ru.zemlyakov.moviesRecommender2.controllers.telegram.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.telegramServices.SendMessageBotService;
import ru.zemlyakov.moviesRecommender2.models.Movie;
import ru.zemlyakov.moviesRecommender2.services.UserService;

import java.util.Set;

public class HistoryCommand implements Command {

    private static final String HISTORY_TEXT = "Ниже представлена твоя история просмотра фильмов :) ";

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
        Set<Movie> history = userService.getHistoryOfViewing(chatId);
        StringBuilder sb = new StringBuilder(HISTORY_TEXT).append("\n\n");

        if (history.isEmpty()) {
            sb.append("Увы, твоя история пуста :(\n" +
                    "Но ты можешь это исправить! Для того чтобы получить рекомендацию используй комманду ***");
        } else {
            for (Movie movie : history)
                sb.append(movie.getTitle()).append("\n");
        }

        return sb.toString();
    }

//    public void saveNewUser(Long chatId, String userName) {
//        User newUser = new User(chatId, userName);
//        System.out.println(newUser);
//        userService.addNewUser(newUser);
//    }
}
