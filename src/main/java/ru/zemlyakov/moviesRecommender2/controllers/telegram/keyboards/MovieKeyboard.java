package ru.zemlyakov.moviesRecommender2.controllers.telegram.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.zemlyakov.moviesRecommender2.models.Movie;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

final public class MovieKeyboard {

    private static class AlwaysViewButton extends InlineKeyboardButton {
        public AlwaysViewButton(@Nullable String title, short yearOfCreate) {
            super();
            setText("Просмотрено");
            setCallbackData("MV^" + title + "^" + yearOfCreate);
        }
    }

    private static class LinkToKPButton extends InlineKeyboardButton {
        public LinkToKPButton(String webURL) {
            super();
            setText("Страница в КП");
            setUrl(webURL);
        }
    }

    public static List<List<InlineKeyboardButton>> getListMovieButtons(Movie movie) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        String titleForCallbackData = movie.getTitle().length() < 23 ? movie.getTitle() : movie.getOriginalTitle();

        keyboard.add(new ArrayList<>(
                        List.of(
                                new AlwaysViewButton(
                                        titleForCallbackData,
                                        movie.getYearOfCreate()
                                ),
                                new LinkToKPButton(movie.getWebURL())
                        )
                )
        );

        return keyboard;
    }

}
