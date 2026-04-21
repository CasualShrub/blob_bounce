package com.bouncefish.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bouncefish.gameplay.ProgressTracker;

public class GameOverScreen {

    private BitmapFont font;
    private Texture overlay;

    public GameOverScreen() {
        font = new BitmapFont();
        font.getData().setScale(3f);

        // Create a semi-transparent black overlay
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 0.6f);
        pixmap.fill();
        overlay = new Texture(pixmap);
        pixmap.dispose();
    }

    public void render(SpriteBatch batch, int score) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        int highScore = ProgressTracker.getHighScore();

        // Note: We don't call batch.begin() here anymore because we want to draw
        // this on top of the already drawing game batch in Main.java

        // 1. Draw the semi-transparent overlay over the game
        batch.draw(overlay, 0, 0, screenWidth, screenHeight);

        // 2. Draw "GAME OVER" title
        font.setColor(Color.ORANGE);
        font.draw(batch, "GAME OVER", screenWidth * 0.5f - 180, screenHeight * 0.75f);

        // 3. Draw Scores
        font.setColor(Color.WHITE);
        font.getData().setScale(2f);
        font.draw(batch, "SCORE: " + score, screenWidth * 0.5f - 100, screenHeight * 0.55f);
        font.draw(batch, "BEST: " + highScore, screenWidth * 0.5f - 100, screenHeight * 0.45f);

        font.getData().setScale(1.5f);
        font.draw(batch, "TAP TO RESTART", screenWidth * 0.5f - 140, screenHeight * 0.25f);
    }

    public boolean isTouched() {
        return Gdx.input.justTouched();
    }

    public void dispose() {
        font.dispose();
        overlay.dispose();
    }
}
