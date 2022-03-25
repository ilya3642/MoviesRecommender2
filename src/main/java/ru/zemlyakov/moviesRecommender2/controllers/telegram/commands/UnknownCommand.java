package ru.zemlyakov.moviesRecommender2.controllers.telegram.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.telegramServices.SendMessageBotService;

public class UnknownCommand implements Command {

    private final SendMessageBotService messageService;

    private static final String UNKNOWN_COMMAND_TEXT = "Данная комманда не распознана :( " +
            "Используйте команду /help для получения списка доступных команд";

    public UnknownCommand(SendMessageBotService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void execute(Update update) {
        messageService.sendMessage(update.getMessage()
                        .getChatId()
                        .toString(),
                UNKNOWN_COMMAND_TEXT
        );
    }
}
