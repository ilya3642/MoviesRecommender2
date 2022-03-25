package ru.zemlyakov.moviesRecommender2.controllers.telegram.keyboards.Callbacks;

import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface Callback {

    void handle(CallbackQuery callbackQuery);

}
