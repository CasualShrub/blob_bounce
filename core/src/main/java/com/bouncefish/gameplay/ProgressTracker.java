package com.bouncefish.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class ProgressTracker {
    private static int score = 0;
    private static int highScore = 0;
    private static int creaturesHit = 0;
    private static Preferences prefs;

    private static void ensureInit() {
        if (prefs == null) {
            prefs = Gdx.app.getPreferences("BounceFishPrefs");
            highScore = prefs.getInteger("highScore", 0);
        }
    }

    public static void increaseScore(){
        increaseScore(1);
    }

    public static void increaseScore(int amount){
        ensureInit();
        score += amount;
        creaturesHit++;
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
        ensureInit();
        return highScore;
    }

    public static int getCreaturesHit() {
        return creaturesHit;
    }

    public static void reset(){
        score = 0;
        creaturesHit = 0;
    }
}
