package com.bouncefish.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class MenuScreen {

    private Texture background;
    private Texture playButton;
    private Texture leaderboardButton;
    private Texture title;
    private Texture settingsButton;

    private Rectangle playBounds;
    private Rectangle leaderboardBounds;
    private Rectangle settingsBounds;

    public MenuScreen() {
        background = new Texture("Menuscreen/Menubackground.png");
        playButton = new Texture("Menuscreen/Play_button (3).png");
        leaderboardButton = new Texture("Menuscreen/Leaderboard_button.png");
        title = new Texture("Menuscreen/Bouncefish_title.png");
        settingsButton = new Texture("Menuscreen/setting_button.png");

        playBounds = new Rectangle();
        leaderboardBounds = new Rectangle();
        settingsBounds = new Rectangle();
    }

    public void render(SpriteBatch batch) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        batch.begin();
        // 1. Draw Background
        batch.draw(background, 0, 0, screenWidth, screenHeight);

        // 2. Draw Title
        float titleWidth = screenWidth * 0.8f;
        float titleHeight = titleWidth * (title.getHeight() / (float)title.getWidth());
        batch.draw(title, (screenWidth - titleWidth) / 2, screenHeight * 0.7f, titleWidth, titleHeight);

        // 3. Draw Buttons
        float btnWidth = screenWidth * 0.5f;
        float btnHeight = btnWidth * (playButton.getHeight() / (float)playButton.getWidth());
        float btnX = (screenWidth - btnWidth) / 2;

        float playY = screenHeight * 0.45f;
        float leaderboardY = screenHeight * 0.32f;
        float settingsY = screenHeight * 0.19f;

        batch.draw(playButton, btnX, playY, btnWidth, btnHeight);
        batch.draw(leaderboardButton, btnX, leaderboardY, btnWidth, btnHeight);
        batch.draw(settingsButton, btnX, settingsY, btnWidth, btnHeight);

        // Update bounds for input
        playBounds.set(btnX, playY, btnWidth, btnHeight);
        leaderboardBounds.set(btnX, leaderboardY, btnWidth, btnHeight);
        settingsBounds.set(btnX, settingsY, btnWidth, btnHeight);

        batch.end();
    }

    public int getButtonPressed() {
        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (playBounds.contains(x, y)) return 1;
            if (leaderboardBounds.contains(x, y)) return 2;
            if (settingsBounds.contains(x, y)) return 3;
        }
        return 0;
    }

    public void dispose() {
        background.dispose();
        playButton.dispose();
        leaderboardButton.dispose();
        title.dispose();
        settingsButton.dispose();
    }
}
