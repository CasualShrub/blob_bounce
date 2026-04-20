
package com.bouncefish.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuScreen {

    private Texture background;
    private Texture playButton;

    private Texture leaderboardButton;

    public MenuScreen() {
        background = new Texture("menu_bg.png");
        playButton = new Texture("play_button.png");
        leaderboardButton = new Texture("leaderboard_button.png");
    }

    public void render(SpriteBatch batch) {

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float buttonWidth = screenWidth * 0.4f;
        float buttonHeight = buttonWidth * 0.4f;

        float centerX = (screenWidth - buttonWidth) / 2;
        float playY = screenHeight * 0.55f;
        float leaderboardY = screenHeight * 0.35f;

        batch.begin();

        batch.draw(background, 0, 0, screenWidth, screenHeight);
        batch.draw(playButton, centerX, playY, buttonWidth, buttonHeight);
        batch.draw(leaderboardButton, centerX, leaderboardY, buttonWidth, buttonHeight);

        batch.end();
    }

    public int getButtonPressed() {

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float buttonWidth = screenWidth * 0.4f;
        float buttonHeight = buttonWidth * 0.4f;

        float centerX = (screenWidth - buttonWidth) / 2;
        float playY = screenHeight * 0.55f;
        float leaderboardY = screenHeight * 0.35f;

        if (Gdx.input.justTouched()) {

            float x = Gdx.input.getX();
            float y = screenHeight - Gdx.input.getY();
            // PLAY
            if (x >= centerX && x <= centerX + buttonWidth
                && y >= playY && y <= playY + buttonHeight) {
                return 1;
            }
            // LEADERBOARD
            if (x >= centerX && x <= centerX + buttonWidth
                && y >= leaderboardY && y <= leaderboardY + buttonHeight) {
                return 2;
            }
        }
        return 0;
    }

    public void dispose() {
        background.dispose();
        playButton.dispose();
    }

}
