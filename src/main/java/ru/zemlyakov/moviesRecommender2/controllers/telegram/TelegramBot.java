package ru.zemlyakov.moviesRecommender2.controllers.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.commands.CommandContainer;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.keyboards.Callbacks.CallbackContainer;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.telegramServices.SendMessageBotServiceImpl;
import ru.zemlyakov.moviesRecommender2.services.GenreService;
import ru.zemlyakov.moviesRecommender2.services.MovieService;
import ru.zemlyakov.moviesRecommender2.services.UserService;

@Controller
public class TelegramBot extends TelegramLongPollingBot {

    private final CommandContainer commandContainer;
    private final CallbackContainer callbackContainer;

    @Autowired
    TelegramBot(UserService userService,
                MovieService movieService,
                GenreService genreService
                ) {
        super();
        this.commandContainer = new CommandContainer(
                new SendMessageBotServiceImpl(this),
                userService,
                movieService,
                genreService);

        this.callbackContainer = new CallbackContainer(
                new SendMessageBotServiceImpl(this),
                userService,
                movieService,
                genreService);

    }

    @Value("${bot.name}")
    private String botName;
    @Value("${bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasCallbackQuery()) {
            String[] callback = update.getCallbackQuery().getData().split("\\^");
            callbackContainer.extractHandler(callback[0]).handle(update.getCallbackQuery());
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            String[] command = update.getMessage().getText().split(" ");
            commandContainer.extractCommand(command[0]).execute(update);
        }
    }

}