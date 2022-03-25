package ru.zemlyakov.moviesRecommender2.controllers.telegram.telegramServices;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public interface SendMessageBotService {

    void sendMessage(String chatId, String message);

    void sendMessage(String chatId, String message, List<List<InlineKeyboardButton>> buttons);

    void editMessage(String chatId, int messageId, List<List<InlineKeyboardButton>> buttons);

    void deleteMessage(String chatId, int messageId);

}
