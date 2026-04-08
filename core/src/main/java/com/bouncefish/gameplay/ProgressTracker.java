package com.bouncefish.gameplay;

public class ProgressTracker {
    private int score;

    public ProgressTracker(){
        score = 0;
    }
    public void increaseScore(){
        score++;
    }

    public int getScore(){
        return score;
    }

    public void reset(){
        score = 0;
    }
}
