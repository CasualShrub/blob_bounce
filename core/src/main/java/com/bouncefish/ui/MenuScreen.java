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

        // 1. Calculating base sizes relative to screen height
        float titleHeight = screenHeight * 0.22f;
        float titleWidth = titleHeight * (title.getWidth() / (float)title.getHeight());
        if (titleWidth > screenWidth * 0.8f) {
            titleWidth = screenWidth * 0.8f;
            titleHeight = titleWidth * (title.getHeight() / (float)title.getWidth());
        }

        float btnHeight = screenHeight * 0.2f;
        float btnWidth = btnHeight * (playButton.getWidth() / (float)playButton.getHeight());

        // 2. Calculateing Vertical Stack (Title + Buttons)
        float titleSpacing = btnHeight * 0.10f;
        float totalHeight = titleHeight + (btnHeight * 3) + titleSpacing;
        float startY = (screenHeight + totalHeight) / 2f;

        batch.begin();
        // Draw Background
        //batch.draw(background, 0, 0, screenWidth, screenHeight);

        // Draw Title (Top of stack)
        float currentY = startY - titleHeight;
        float titleX = (screenWidth - titleWidth) / 2f;
        float shadowOffset = titleHeight * 0.04f;
        batch.setColor(0, 0, 0, 0.4f);
        batch.draw(title, titleX + shadowOffset, currentY - shadowOffset, titleWidth, titleHeight);
        batch.setColor(1, 1, 1, 1f);
        batch.draw(title, titleX, currentY, titleWidth, titleHeight);

        // Draw Buttons below Title
        float btnX = (screenWidth - btnWidth) / 2f;

        currentY -= (btnHeight + titleSpacing);
        batch.draw(playButton, btnX, currentY, btnWidth, btnHeight);
        playBounds.set(btnX, currentY, btnWidth, btnHeight);

        currentY -= (btnHeight);
        batch.draw(leaderboardButton, btnX, currentY, btnWidth, btnHeight);
        leaderboardBounds.set(btnX, currentY, btnWidth, btnHeight);

        currentY -= (btnHeight);
        batch.draw(settingsButton, btnX, currentY, btnWidth, btnHeight);
        settingsBounds.set(btnX, currentY, btnWidth, btnHeight);

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
