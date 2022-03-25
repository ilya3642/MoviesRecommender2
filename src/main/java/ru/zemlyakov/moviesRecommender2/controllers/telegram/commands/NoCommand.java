package ru.zemlyakov.moviesRecommender2.controllers.telegram.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.telegramServices.SendMessageBotService;

public class NoCommand implements Command{

    private final SendMessageBotService messageService;

    private static final String NO_COMMAND_TEXT = "Вы не ввели команду :( " +
            "Используйте команду /help для получения списка доступных команд!";

    public NoCommand(SendMessageBotService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void execute(Update update) {
        messageService.sendMessage(update
                .getMessage()
                .getChatId()
                .toString(),
                NO_COMMAND_TEXT);
    }

}
