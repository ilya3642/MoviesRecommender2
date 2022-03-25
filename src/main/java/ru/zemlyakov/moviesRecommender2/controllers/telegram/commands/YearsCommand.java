package ru.zemlyakov.moviesRecommender2.controllers.telegram.commands;

import org.telegram.telegrambots.meta.api.objects.Update;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.telegramServices.SendMessageBotService;
import ru.zemlyakov.moviesRecommender2.models.User;
import ru.zemlyakov.moviesRecommender2.services.UserService;

import java.time.LocalDate;

public class YearsCommand implements Command {

    private final SendMessageBotService messageService;
    private final UserService userService;

    private static final String SET_YEARS_TEXT = "Фильтр фильмов по году их выпуска, " +
            "Текущие настройки *минимальный год* = %d, *максимальный год* = %d\n" +
            "Для изменения введи команду setyears и введи минимальный и максимальный года через пробел, например, так\n" +
            "setyears 1990 2022";

    public YearsCommand(
            SendMessageBotService messageService,
            UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @Override
    public void execute(Update update) {
        Long chatId = update.getMessage().getChatId();
        User user = userService.getUser(chatId);
        String setYearsText = String.format(
                SET_YEARS_TEXT,
                user.getMinYearOfCreateMovie(),
                user.getMaxYearOfCreateMovie()
        );

        messageService.sendMessage(chatId.toString(), setYearsText);

    }

    public static class SetYearCommand implements Command{

        private final SendMessageBotService messageService;
        private final UserService userService;

        public SetYearCommand(
                SendMessageBotService messageService,
                UserService userService) {
            this.messageService = messageService;
            this.userService = userService;
        }

        @Override
        public void execute(Update update) {
            Long chatId = update.getMessage().getChatId();
            User user = userService.getUser(chatId);
            String[] years = update.getMessage().getText().split(" ");

            try {
                short minYear = Short.parseShort(years[1]);
                short maxYear = Short.parseShort(years[2]);
                if (minYear > 1899 && minYear <= LocalDate.now().getYear() && minYear <= maxYear) {
                    user.setMinYearOfCreateMovie(minYear);
                    user.setMaxYearOfCreateMovie(maxYear);
                    user.refreshPageOfRecommend();
                    userService.saveOrUpdate(user);
                }
                else throw new NumberFormatException("Неверный формат чисел");
            } catch (Exception ex){
                messageService.sendMessage(chatId.toString(), "Данные введены некорректно:" +
                        "Года должны быть не меньше 1899 и не больше текущего, а также минимальный год не может быть больше максимального");
                ex.printStackTrace();
            }

            new YearsCommand(messageService, userService).execute(update);
        }
    }
}
