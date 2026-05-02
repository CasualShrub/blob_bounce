package com.bouncefish.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.bouncefish.gameplay.ProgressTracker;

public class GameOverScreen {

    private Texture background;
    private Texture title;
    private Texture playAgainButton;
    private Texture menuButton;
    private BitmapFont font;
    private GlyphLayout layout;

    private Rectangle playAgainBounds;
    private Rectangle menuBounds;

    private int actionPressed = 0; // 0: none, 1: play again, 2: menu

    public GameOverScreen() {
        background = new Texture("GameoverScreen/GameoverScreen.png");
        title = new Texture("GameoverScreen/gameovertitle.png");
        playAgainButton = new Texture("GameoverScreen/playagain_button.png");
        menuButton = new Texture("GameoverScreen/Gameover_Menu button.png");

        font = new BitmapFont();
        layout = new GlyphLayout();

        playAgainBounds = new Rectangle();
        menuBounds = new Rectangle();
    }

    public void render(SpriteBatch batch, int score) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        int highScore = ProgressTracker.getHighScore();

        // 1. Defining base sizes relative to screen height (safer for tall phones)
        float titleHeight = screenHeight * 0.15f;
        float titleWidth = titleHeight * (title.getWidth() / (float)title.getHeight());

        // If title is too wide, cap it
        if (titleWidth > screenWidth * 0.8f) {
            titleWidth = screenWidth * 0.8f;
            titleHeight = titleWidth * (title.getHeight() / (float)title.getWidth());
        }

        float panelHeight = screenHeight * 0.45f;
        float panelWidth = panelHeight * (background.getWidth() / (float)background.getHeight());

        if (panelWidth > screenWidth * 0.9f) {
            panelWidth = screenWidth * 0.9f;
            panelHeight = panelWidth * (background.getHeight() / (float)background.getWidth());
        }

        float btnHeight = screenHeight * 0.08f;
        float btnWidth = btnHeight * (playAgainButton.getWidth() / (float)playAgainButton.getHeight());

        // 2. Calculating the Vertical "Stack" Center
        float spacing = screenHeight * 0.02f;
        float totalHeight = titleHeight + panelHeight + (btnHeight * 2) + (spacing * 3);
        float startY = (screenHeight + totalHeight) / 2f;

        // 3. Drawing Title (Top)
        float currentY = startY - titleHeight;
        batch.draw(title, (screenWidth - titleWidth) / 2f, currentY, titleWidth, titleHeight);

        // 4. Drawing Panel (Middle)
        currentY -= (panelHeight + spacing);
        float panelX = (screenWidth - panelWidth) / 2f;
        float panelY = currentY;
        batch.draw(background, panelX, panelY, panelWidth, panelHeight);

        // 5. Drawing Scores inside Panel
        font.getData().setScale(screenWidth / 450f);

        // YOUR SCORE
        String scoreStr = String.valueOf(score);
        layout.setText(font, scoreStr);
        font.draw(batch, scoreStr, (screenWidth - layout.width) / 2f, panelY + panelHeight * 0.62f);

        // HIGH SCORE
        font.getData().setScale(screenWidth / 550f);
        String highStr = String.valueOf(highScore);
        layout.setText(font, highStr);
        // Positioned under the High Score label on the left side
        font.draw(batch, highStr, panelX + panelWidth * 0.38f - layout.width / 2f, panelY + panelHeight * 0.46f);

        // 6. Draw Buttons (Bottom)
        float btnX = (screenWidth - btnWidth) / 2f;

        // Play Again button
        currentY -= (btnHeight + spacing);
        batch.draw(playAgainButton, btnX, currentY, btnWidth, btnHeight);
        playAgainBounds.set(btnX, currentY, btnWidth, btnHeight);

        // Menu button
        currentY -= (btnHeight + spacing * 0.5f);
        batch.draw(menuButton, btnX, currentY, btnWidth, btnHeight);
        menuBounds.set(btnX, currentY, btnWidth, btnHeight);

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
        actionPressed = 0;
        return action;
    }

    public void dispose() {
        background.dispose();
        title.dispose();
        playAgainButton.dispose();
        menuButton.dispose();
        font.dispose();
    }
}
