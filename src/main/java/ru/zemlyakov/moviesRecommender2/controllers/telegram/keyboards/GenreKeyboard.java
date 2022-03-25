package ru.zemlyakov.moviesRecommender2.controllers.telegram.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.zemlyakov.moviesRecommender2.models.Genre;

import java.util.ArrayList;
import java.util.List;

final public class GenreKeyboard {

    private static class GenreButton extends InlineKeyboardButton {
        public GenreButton(String genreName) {
            super();
            setText(genreName);
            setCallbackData("GENRE^" + genreName);
        }
    }

    public static List<List<InlineKeyboardButton>> getListGenreButtons(List<Genre> allGenres, List<Genre> userFavouriteGenres) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();
        List<InlineKeyboardButton> rawButtons = new ArrayList<>(6);

        for (Genre genre : allGenres) {
            String textButton = genre.getGenreName();
            if (userFavouriteGenres.contains(genre)) {
                textButton += " " + "✅";
            }
            rawButtons.add(new GenreButton(textButton));
        }

        for (int i = 0; i < rawButtons.size(); i += 3) {
            if (i + 3 < rawButtons.size())
                keyboard.add(rawButtons.subList(i, i + 3));
            else {
                keyboard.add(rawButtons.subList(i, rawButtons.size()));
                break;
            }
        }
        return keyboard;
    }

}
