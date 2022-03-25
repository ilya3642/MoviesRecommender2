package ru.zemlyakov.moviesRecommender2.controllers.telegram.commands;

public enum CommandName {

    START("/start"),
    HELP("/help"),
    STOP("/stop"),
    NO("noCommand"),
    HISTORY("/history"),
    RECOMMEND("/recommend"),
    FAVOURITE("/favorite"),
    YEARS("/years"),
    SET_YEARS("setyears");

    private final String commandName;

    CommandName(String commandName){
        this.commandName = commandName;
    }

    public String getCommandName(){
        return commandName;
    }

}
