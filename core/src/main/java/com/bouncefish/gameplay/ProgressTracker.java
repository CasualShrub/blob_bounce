package com.bouncefish.gameplay;

public class ProgressTracker {
    private static int score = 0;

    public static void increaseScore(){
        score++;
    }

    public static int getScore(){
        return score;
    }

    public static void reset(){
        score = 0;
    }
}
