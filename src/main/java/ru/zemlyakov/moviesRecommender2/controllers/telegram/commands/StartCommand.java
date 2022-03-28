package ru.zemlyakov.moviesRecommender2.controllers.telegram.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.telegramServices.SendMessageBotService;
import ru.zemlyakov.moviesRecommender2.models.User;
import ru.zemlyakov.moviesRecommender2.services.GenreService;
import ru.zemlyakov.moviesRecommender2.services.UserService;

public class StartCommand implements Command {

    private static final String START_TEXT = "Привет! Я бот для рекомендации фильмов :) " +
            "Я могу запоминать просмотренные тобой фильмы и предлагать к просмотру новые, " +
            "основываясь на твоих предпочтениях и мировых оценках пользователей и критиков!\n\n";

    //private static final String START_TEXT_NEW_USER = "Давай начнём с зака, какие из следующих фильмов ты уже видел?";

    private final SendMessageBotService messageService;
    private final UserService userService;
    private final GenreService genreService;

    public StartCommand(SendMessageBotService messageService,
                        UserService userService,
                        GenreService genreService) {
        this.messageService = messageService;
        this.userService = userService;
        this.genreService = genreService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        messageService.sendMessage(chatId.toString(), START_TEXT);
        userService.saveOrUpdate(new User(chatId, update.getMessage().getChat().getFirstName()));
        new FavoriteCommand(messageService, userService, genreService).execute(update);
    }

}
