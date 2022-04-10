package ru.zemlyakov.moviesRecommender2.controllers.telegram.telegramServices;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.TelegramBot;

import java.util.List;

@Service
public class SendMessageBotServiceImpl implements SendMessageBotService {

    private final TelegramBot telegramBot;

    @Autowired
    public SendMessageBotServiceImpl(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @Override
    public void sendMessage(String chatId, String message) {

        try {
            telegramBot.execute(
                    SendMessage.builder()
                            .parseMode("Markdown")
                            .chatId(chatId)
                            .text(message)
                            .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void sendMessage(String chatId, String message, List<List<InlineKeyboardButton>> buttons) {
        try {
            telegramBot.execute(
                    SendMessage.builder()
                            .chatId(chatId)
                            .parseMode("Markdown")
                            .text(message)
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                            .build()
            );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void editMessageKeyboard(String chatId, int messageId, List<List<InlineKeyboardButton>> buttons) {
        try {
            telegramBot.execute(
                    EditMessageReplyMarkup.builder()
                            .chatId(chatId)
                            .messageId(messageId)
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                            .build()
            );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void editMessage(String chatId, int messageId, String message, List<List<InlineKeyboardButton>> buttons) {

        try {
            telegramBot.execute(
                    EditMessageText.builder()
                            .chatId(chatId)
                            .parseMode("Markdown")
                            .messageId(messageId)
                            .text(message)
                            .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                            .build()
            );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteMessage(String chatId, int messageId) {
        try {
            telegramBot.execute(
                    DeleteMessage.builder()
                            .chatId(chatId)
                            .messageId(messageId)
                            .build()
            );
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}