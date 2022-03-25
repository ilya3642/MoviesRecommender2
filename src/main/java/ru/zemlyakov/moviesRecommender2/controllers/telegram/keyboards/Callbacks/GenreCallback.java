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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Matcher matcher = Pattern.compile("(?U)[\\w]+").matcher(callbackQuery.getData());
        matcher.find();
        matcher.find();
        Long chatId = message.getChatId();
        User user = userService.getUserWithGenres(chatId);
        Genre newFavouriteGenre = genreService.getGenre(matcher.group());
        Set<Genre> favouriteGenres = user.getFavouriteGenres();

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
                        new ArrayList<>(user.getFavouriteGenres())
                )
        );

    }

}
