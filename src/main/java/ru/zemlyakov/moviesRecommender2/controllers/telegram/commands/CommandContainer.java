package ru.zemlyakov.moviesRecommender2.controllers.telegram.commands;

import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;
import ru.zemlyakov.moviesRecommender2.controllers.telegram.telegramServices.SendMessageBotService;
import ru.zemlyakov.moviesRecommender2.services.GenreService;
import ru.zemlyakov.moviesRecommender2.services.MovieService;
import ru.zemlyakov.moviesRecommender2.services.UserService;

import static ru.zemlyakov.moviesRecommender2.controllers.telegram.commands.CommandName.*;

@Component
public class CommandContainer {

    private final ImmutableMap<String, Command> commandsMap;
    private final Command unknownCommand;

    public CommandContainer(
            SendMessageBotService sendMessageBotService,
            UserService userService,
            MovieService movieService,
            GenreService genreService) {
        commandsMap = ImmutableMap.<String, Command>builder()
                .put(START.getCommandName(), new StartCommand(sendMessageBotService, userService, genreService))
                .put(STOP.getCommandName(), new StopCommand(sendMessageBotService))
                .put(NO.getCommandName(), new NoCommand(sendMessageBotService))
                .put(HELP.getCommandName(), new HelpCommand(sendMessageBotService))
                .put(HISTORY.getCommandName(), new HistoryCommand(sendMessageBotService, userService))
                .put(RECOMMEND.getCommandName(), new RecommendCommand(sendMessageBotService, userService, movieService))
                .put(FAVOURITE.getCommandName(), new FavoriteCommand(sendMessageBotService, userService, genreService))
                .put(YEARS.getCommandName(), new YearsCommand(sendMessageBotService, userService))
                .put(SET_YEARS.getCommandName(), new YearsCommand.SetYearCommand(sendMessageBotService, userService))
                .build();

        unknownCommand = new UnknownCommand(sendMessageBotService);
    }

    public Command extractCommand(String commandIdentifier){
        return commandsMap.getOrDefault(commandIdentifier, unknownCommand);
    }



}
