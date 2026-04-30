package com.bouncefish.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.bouncefish.gameplay.ProgressTracker;

public class GameOverScreen {

    private Texture background;
    private Texture playAgainButton;
    private Texture menuButton;
    private BitmapFont font;

    private Rectangle playAgainBounds;
    private Rectangle menuBounds;

    private int actionPressed = 0; // 0: none, 1: play again, 2: menu

    public GameOverScreen() {
        background = new Texture("GameoverScreen/GameoverScreen.png");
        playAgainButton = new Texture("GameoverScreen/playagain_button.png");
        menuButton = new Texture("GameoverScreen/Gameover_Menu button.png");

        font = new BitmapFont();
        font.getData().setScale(2f);

        playAgainBounds = new Rectangle();
        menuBounds = new Rectangle();
    }

    public void render(SpriteBatch batch, int score) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        int highScore = ProgressTracker.getHighScore();

        // 1. Draw the main Game Over panel (The one with the coral/underwater theme)
        // Center it or scale it to fit.
        float panelWidth = screenWidth * 0.8f;
        float panelHeight = panelWidth * (background.getHeight() / (float)background.getWidth());
        float panelX = (screenWidth - panelWidth) / 2;
        float panelY = (screenHeight - panelHeight) / 2;

        batch.draw(background, panelX, panelY, panelWidth, panelHeight);

        // 2. Draw Scores
        // need to adjust these coordinates based on the transparent areas of your PNG
        font.draw(batch, String.valueOf(score), screenWidth * 0.5f - 20, panelY + panelHeight * 0.65f);
        font.draw(batch, String.valueOf(highScore), panelX + panelWidth * 0.35f, panelY + panelHeight * 0.45f);

        // 3. Draw Buttons
        float btnWidth = panelWidth * 0.7f;
        float btnHeight = btnWidth * (playAgainButton.getHeight() / (float)playAgainButton.getWidth());
        float btnX = (screenWidth - btnWidth) / 2;

        float playY = panelY + panelHeight * 0.25f;
        float menuY = panelY + panelHeight * 0.1f;

        batch.draw(playAgainButton, btnX, playY, btnWidth, btnHeight);
        batch.draw(menuButton, btnX, menuY, btnWidth, btnHeight);

        // Update click bounds
        playAgainBounds.set(btnX, playY, btnWidth, btnHeight);
        menuBounds.set(btnX, menuY, btnWidth, btnHeight);

        handleInput();
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (playAgainBounds.contains(x, y)) {
                actionPressed = 1;
            } else if (menuBounds.contains(x, y)) {
                actionPressed = 2;
            }
        }
    }

    public int getActionPressed() {
        int action = actionPressed;
        actionPressed = 0; // Reset after reading
        return action;
    }

    public void dispose() {
        background.dispose();
        playAgainButton.dispose();
        menuButton.dispose();
        font.dispose();
    }
}
