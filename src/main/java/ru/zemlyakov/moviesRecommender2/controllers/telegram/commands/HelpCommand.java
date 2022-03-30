package ru.zemlyakov.moviesRecommender2.controllers.telegram.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.telegramServices.SendMessageBotService;

public class HelpCommand implements Command {

    private final SendMessageBotService messageService;

    private static final String HELP_TEXT = "Я бот, для рекомендации фильмов :) " +
            "Я могу запомминать просмотренные тобой фильмы и предлагать к просмотру новые, " +
            "основываясь на твоих предпочтениях и мировых оценках пользователей и критиков!" +
            "Список доступных комманд:\n" +
            "/start - начало работы с ботом\n" +
            "/recommend - получить рекомендацию\n" +
            "/years - получить информацию о фильтре по году выпуска\n" +
            "/history - получить историю просмотров\n" +
            "/help - справка по доступным коммандам\n" +
            "/stop - прекратить использование бота\n";

    public HelpCommand(SendMessageBotService messageService) {
        this.messageService = messageService;
    }

    @Override
    public void execute(Update update) {
        messageService.sendMessage(
                update.getMessage().getChatId().toString(),
                HELP_TEXT
        );
    }

}
