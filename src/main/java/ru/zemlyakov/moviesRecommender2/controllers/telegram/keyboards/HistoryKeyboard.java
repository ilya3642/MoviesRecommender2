package ru.zemlyakov.moviesRecommender2.controllers.telegram.keyboards;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ru.zemlyakov.moviesRecommender2.models.Movie;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class HistoryKeyboard {

    private static class LinkToBack extends InlineKeyboardButton {
        public LinkToBack(int numPage) {
            super();
            setText("⬅");
            setCallbackData("HSTR^" + numPage);
        }
    }

    private static class LinkToNext extends InlineKeyboardButton {
        public LinkToNext(int numPage) {
            super();
            setText("➡");
            setCallbackData("HSTR^"+numPage);
        }
    }

    private static class LinkToFirst extends InlineKeyboardButton {
        public LinkToFirst() {
            super();
            setText("1");
            setCallbackData("HSTR^1");
        }
    }

    private static class LinkToLast extends InlineKeyboardButton {
        public LinkToLast(int countMovie) {
            super();
            setText("Страница в КП");
            setCallbackData("HSTR^" + countMovie);
        }
    }

    private static class CounterPage extends InlineKeyboardButton {
        public CounterPage(int currPage) {
            super();
            setText(String.valueOf(currPage));
        }
    }

    private static class DeleteFromHistory extends InlineKeyboardButton {
        public DeleteFromHistory(@Nullable String title, short yearOfCreate) {
            super();
            setText("Удалить из истории");
            setCallbackData("HSTR^" + title + "^" + yearOfCreate);
        }
    }

    private static class CloseHistory extends InlineKeyboardButton {
        public CloseHistory(@Nullable String title, short yearOfCreate) {
            super();
            setText("Удалить из истории");
            setCallbackData("HSTR^" + title + "^" + yearOfCreate);
        }
    }

    public static List<List<InlineKeyboardButton>> getListHistoryButtons(int currPage, int maxPage) {
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        int backPage;
        if (currPage == 0)
            backPage = 0;
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

        

//        String titleForCallbackData = movie.getTitle().length() < 23 ? movie.getTitle() : movie.getOriginalTitle();
//
//        keyboard.add(
//                new ArrayList<>(
//                        List.of(
//                                new MovieKeyboard.AlwaysViewButton(
//                                        titleForCallbackData,
//                                        movie.getYearOfCreate()
//                                ),
//                                new MovieKeyboard.LinkToKPButton(movie.getWebURL())
//                        )
//                )
//        );

        return keyboard;
    }

}

