package ru.zemlyakov.moviesRecommender2.controllers.telegram.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.telegramServices.SendMessageBotService;

public class StopCommand implements Command {

    private final SendMessageBotService messageService;

    private static final String STOP_TEXT = "Я буду ждать твоего возвращения!";

    public StopCommand(SendMessageBotService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void execute(Update update) {

        messageService.sendMessage(update.getMessage()
                        .getChatId()
                        .toString(),
                STOP_TEXT);
    }
}
