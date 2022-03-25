package ru.zemlyakov.moviesRecommender2.controllers.telegram.keyboards.Callbacks;

public enum CallbackName {

    GENRE("GENRE"),
    MOVIE("MV");

    private final String callbackName;

    CallbackName(String commandName){
        this.callbackName = commandName;
    }

    public String getCallbackName(){
        return callbackName;
    }
}
