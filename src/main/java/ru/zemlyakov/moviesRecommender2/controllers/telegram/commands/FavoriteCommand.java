package ru.zemlyakov.moviesRecommender2.controllers.telegram.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.keyboards.GenreKeyboard;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.telegramServices.SendMessageBotService;
import ru.zemlyakov.moviesRecommender2.models.Genre;
import ru.zemlyakov.moviesRecommender2.models.User;
import ru.zemlyakov.moviesRecommender2.services.GenreService;
import ru.zemlyakov.moviesRecommender2.services.UserService;

import java.util.ArrayList;
import java.util.List;

public class FavoriteCommand implements Command {

    private final SendMessageBotService messageService;
    private final UserService userService;
    private final GenreService genreService;

    private static final String FAVOURITE_TEXT = "Давай выберем твои любимые жанры, " +
            "на основе твоего выбора я буду рекомендовать наилучшие фильмы\n";

    public FavoriteCommand(SendMessageBotService messageService,
                           UserService userService,
                           GenreService genreService) {
        this.messageService = messageService;
        this.userService = userService;
        this.genreService = genreService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        List<Genre> genres = genreService.getAllGenres();
        User user = userService.getUserWithGenres(chatId);

        messageService.sendMessage(
                chatId.toString(),
                FAVOURITE_TEXT,
                GenreKeyboard.getListGenreButtons(
                        genres,
                        new ArrayList<>(user.getFavoriteGenres()
                        )
                )
        );
    }
}
