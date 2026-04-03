
package com.bouncefish.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MenuScreen {

    private Texture background;
    private Texture playButton;

    public MenuScreen() {
        background = new Texture("menu_bg.png");
        playButton = new Texture("play_button.png");
    }

    public void render(SpriteBatch batch) {

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Button scaling
        float playWidth = screenWidth * 0.4f;
        float playHeight = playWidth * 0.4f;

        // Center position
        float playX = (screenWidth - playWidth) / 2;
        float playY = (screenHeight - playHeight) / 2;

        // Draw background (full screen)
        batch.draw(background, 0, 0, screenWidth, screenHeight);

        // Draw play button (centered)
        batch.draw(playButton, playX, playY, playWidth, playHeight);
    }

    public boolean isPlayPressed() {

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float playWidth = screenWidth * 0.4f;
        float playHeight = playWidth * 0.4f;

        float playX = (screenWidth - playWidth) / 2;
        float playY = (screenHeight - playHeight) / 2;

        if (Gdx.input.justTouched()) {

            float touchX = Gdx.input.getX();
            float touchY = screenHeight - Gdx.input.getY();

            if (touchX >= playX && touchX <= playX + playWidth &&
                touchY >= playY && touchY <= playY + playHeight) {
                return true;
            }
        }

        return false;
    }

    public void dispose() {
        background.dispose();
        playButton.dispose();
    }

}
