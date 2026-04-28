package com.bouncefish.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ProgressTracker {
    private static int score = 0;
    private static int highScore = 0;
    private static Preferences prefs;

    static {
        prefs = Gdx.app.getPreferences("BounceFishPrefs");
        highScore = prefs.getInteger("highScore", 0);
    }

    public static void increaseScore(){
        score++;
        if (score > highScore) {
            highScore = score;
            prefs.putInteger("highScore", highScore);
            prefs.flush();
        }
    }

    public static void increaseScore(int points){
        score += points;
        if (score > highScore) {
            highScore = score;
            prefs.putInteger("highScore", highScore);
            prefs.flush();
        }
    }

    public static int getScore(){
        return score;
    }

    public static int getHighScore() {
        return highScore;
    }

    public static void reset(){
        score = 0;
    }
}
