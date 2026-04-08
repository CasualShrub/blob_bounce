package com.bouncefish.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LeaderboardScreen {

    private Texture background;
    private Texture backButton;
    private BitmapFont font;

    public LeaderboardScreen() {
        background = new Texture("leaderboard_bg.png");
        backButton = new Texture("back_button.png");
        font = new BitmapFont();
    }

    public void render(SpriteBatch batch, int score) {

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        batch.begin();

        batch.draw(background, 0, 0, screenWidth, screenHeight);

        // Title
        font.draw(batch, "LEADERBOARD", screenWidth / 2f - 60, screenHeight - 50);

        // Score display
        font.draw(batch, "Score: " + score, screenWidth / 2f - 40, screenHeight - 120);

        // Back button
        float btnWidth = screenWidth * 0.3f;
        float btnHeight = btnWidth * 0.4f;
        float btnX = (screenWidth - btnWidth) / 2;
        float btnY = 50;

        batch.draw(backButton, btnX, btnY, btnWidth, btnHeight);

        batch.end();
    }

    public boolean isBackPressed() {

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float btnWidth = screenWidth * 0.3f;
        float btnHeight = btnWidth * 0.4f;
        float btnX = (screenWidth - btnWidth) / 2;
        float btnY = 50;

        if (Gdx.input.justTouched()) {

            float x = Gdx.input.getX();
            float y = screenHeight - Gdx.input.getY();

            if (x >= btnX && x <= btnX + btnWidth &&
                y >= btnY && y <= btnY + btnHeight) {
                return true;
            }
        }

        return false;
    }

    public void dispose() {
        background.dispose();
        backButton.dispose();
        font.dispose();
    }

}
