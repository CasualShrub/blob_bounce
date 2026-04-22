package com.bouncefish.gameplay;

public class GameStateHandler {

    private static GameState currentState;

    public static GameState getCurrentState(){
        return currentState;
    }

    public static void setCurrentState(GameState newState){
        currentState = newState;
    }
}
