package ru.zemlyakov.moviesRecommender2.controllers.telegram.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.zemlyakov.moviesRecommender2.models.Movie;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class HistoryKeyboard {

    private static class LinkToBack extends InlineKeyboardButton {
        public LinkToBack(int numOfPage) {
            super();
            setText("⬅");
            setCallbackData("HST^" + numOfPage);
        }
    }

    private static class LinkToNext extends InlineKeyboardButton {
        public LinkToNext(int numOfPage) {
            super();
            setText("➡");
            setCallbackData("HST^" + numOfPage);
        }
    }

    private static class LinkToFirst extends InlineKeyboardButton {
        public LinkToFirst() {
            super();
            setText("1");
            setCallbackData("HST^1");
        }
    }

    private static class LinkToLast extends InlineKeyboardButton {
        public LinkToLast(int countMovie) {
            super();
            setText(String.valueOf(countMovie));
            setCallbackData("HST^" + countMovie);
        }
    }

    private static class CounterPage extends InlineKeyboardButton {
        public CounterPage(int currPage) {
            super();
            setText(String.valueOf(currPage));
            setCallbackData("HST^-1");
        }
    }

    private static class DeleteFromHistory extends InlineKeyboardButton {
        public DeleteFromHistory(@Nullable String title, short yearOfCreate) {
            super();
            setText("Удалить из истории");
            setCallbackData("HST^" + title + "^" + yearOfCreate);
        }
    }

    private static class CloseHistory extends InlineKeyboardButton {
        public CloseHistory() {
            super();
            setText("Закрыть историю");
            setCallbackData("HST^close");
        }
    }

    public static List<List<InlineKeyboardButton>> getListHistoryButtons(
            Movie currMovie,
            int currPage,
            int maxPage) {

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        int backPage;
        if (currPage == 1) {
            backPage = 1;
        }
        else backPage = currPage - 1;

        int nextPage;
        if (currPage == maxPage)
            nextPage = currPage;
        else nextPage = currPage + 1;

        keyboard.add(
                new ArrayList<>(
                        List.of(
                                new HistoryKeyboard.LinkToFirst(),
                                new HistoryKeyboard.LinkToBack(backPage),
                                new HistoryKeyboard.CounterPage(currPage),
                                new HistoryKeyboard.LinkToNext(nextPage),
                                new HistoryKeyboard.LinkToLast(maxPage)
                        )
                )
        );

        String titleForCallbackData = currMovie.getTitle().length() < 24 ? currMovie.getTitle() : currMovie.getOriginalTitle();

        keyboard.add(
                new ArrayList<>(
                        List.of(
                                new HistoryKeyboard.DeleteFromHistory(
                                        titleForCallbackData,
                                        currMovie.getYearOfCreate()
                                )
                        )
                )
        );

        keyboard.add(
                new ArrayList<>(
                        List.of(
                                new HistoryKeyboard.CloseHistory()
                        )
                )
        );

        return keyboard;
    }

}

