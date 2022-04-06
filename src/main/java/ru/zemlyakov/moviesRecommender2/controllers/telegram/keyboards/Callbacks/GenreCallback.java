package ru.zemlyakov.moviesRecommender2.controllers.telegram.keyboards.Callbacks;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.keyboards.GenreKeyboard;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.telegramServices.SendMessageBotService;
import ru.zemlyakov.moviesRecommender2.models.Genre;
import ru.zemlyakov.moviesRecommender2.models.User;
import ru.zemlyakov.moviesRecommender2.services.GenreService;
import ru.zemlyakov.moviesRecommender2.services.UserService;

import java.util.ArrayList;
import java.util.Set;

public class GenreCallback implements Callback {

    private final SendMessageBotService messageService;
    private final UserService userService;
    private final GenreService genreService;

    public GenreCallback(SendMessageBotService messageService,
                         UserService userService,
                         GenreService genreService) {
        this.messageService = messageService;
        this.userService = userService;
        this.genreService = genreService;
    }

    @Override
    public void handle(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String callbackData = callbackQuery.getData();
        StringBuilder genreName = new StringBuilder(callbackData);
        genreName.delete(0, 6);

        if (callbackData.contains("✅")) {
            genreName.delete(genreName.length() - 2, genreName.length());
        }

        Genre newFavouriteGenre = genreService.getGenre(genreName.toString());
        Long chatId = message.getChatId();
        User user = userService.getUserWithGenres(chatId);
        Set<Genre> favouriteGenres = user.getFavoriteGenres();

        if (favouriteGenres.contains(newFavouriteGenre))
            favouriteGenres.remove(newFavouriteGenre);
        else
            favouriteGenres.add(newFavouriteGenre);

        user.refreshPageOfRecommend();
        userService.saveOrUpdate(user);
        messageService.editMessage(
                chatId.toString(),
                message.getMessageId(),
                GenreKeyboard.getListGenreButtons(
                        genreService.getAllGenres(),
                        new ArrayList<>(user.getFavoriteGenres())
                )
        );

    }

}
