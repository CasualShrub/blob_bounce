package com.bouncefish.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.bouncefish.gameplay.ProgressTracker;

public class GameOverScreen {

    private Texture background;
    private Texture title;
    private Texture playAgainButton;
    private Texture menuButton;

    private Texture overlay;

    private BitmapFont font;
    private GlyphLayout layout;

    private Rectangle playAgainBounds;
    private Rectangle menuBounds;

    private int actionPressed = 0; // 0: none, 1: play again, 2: menu
    private float stateTime = 0f;

    public GameOverScreen() {
        background = new Texture("GameoverScreen/Gameoverscreenbackground.png");
        title = new Texture("GameoverScreen/gameovertitle.png");
        playAgainButton = new Texture("GameoverScreen/playagain_button.png");
        menuButton = new Texture("GameoverScreen/Gameover_Menu button.png");

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        overlay = new Texture(pixmap);
        pixmap.dispose();

        font = new BitmapFont();
        layout = new GlyphLayout();

        playAgainBounds = new Rectangle();
        menuBounds = new Rectangle();
    }

    public void render(SpriteBatch batch, int score, int creaturesHit) {
        stateTime += Gdx.graphics.getDeltaTime();

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // 0. Draw Dimmer Overlay
        batch.setColor(0, 0, 0, 0.65f);
        batch.draw(overlay, 0, 0, screenWidth, screenHeight);
        batch.setColor(1, 1, 1, 1);

        // 1. Panel sizes relative to screen
        float panelHeight = screenHeight * 0.5f;
        float panelWidth = panelHeight * (background.getWidth() / (float) background.getHeight());

        if (panelWidth > screenWidth * 0.95f) {
            panelWidth = screenWidth * 0.95f;
            panelHeight = panelWidth * (background.getHeight() / (float) background.getWidth());
        }

        float panelX = (screenWidth - panelWidth) / 2f;
        float panelY = (screenHeight - panelHeight) / 2f;

        // 2. Draw Title
        float titleWidth = screenWidth * 0.25f;
        float titleHeight = titleWidth * (title.getHeight() / (float) title.getWidth());
        float titleX = (screenWidth - titleWidth) / 2f;

        float panelTopY = panelY + panelHeight;
        float availableSpaceAtTop = screenHeight - panelTopY;

        // --- MODIFIED HERE ---
        // Instead of dividing by 2 (centering it high up), we multiply by 0.15f
        // to make it sit much lower, hovering just right above the background panel.
        // If you want it even closer, change 0.15f to 0.05f. If you want it touching, change to 0.0f.
        float targetTitleY = panelTopY + (availableSpaceAtTop - titleHeight) * 0.75f;

        float startTitleY = screenHeight * 1.2f;
        float animProgress = Math.min(1f, stateTime / 1.2f);
        float bounceEased = Interpolation.bounceOut.apply(animProgress);
        float currentTitleY = startTitleY + (targetTitleY - startTitleY) * bounceEased;

        batch.draw(title, titleX, currentTitleY, titleWidth, titleHeight);

        // 3. Draw Background Panel
        batch.draw(background, panelX, panelY, panelWidth, panelHeight);

        // 4. Draw Flappy Bird Style Text & Scores
        float centerX = panelX + panelWidth / 2f;
        float leftX = panelX + panelWidth * 0.25f;
        float rightX = panelX + panelWidth * 0.80f;

        // Y-Coordinates for Labels and Numbers
        float topLabelY = panelY + panelHeight * 0.85f;
        float topNumY   = panelY + panelHeight * 0.72f;

        float bottomLabelY = panelY + panelHeight * 0.52f;
        float bottomNumY   = panelY + panelHeight * 0.39f;

        float labelScale = screenWidth / 1400f;
        float numberScale = screenWidth / 850f;

        // TOP CENTER: Current score
        drawTextWithShadow(batch, "YOUR SCORE", centerX, topLabelY, labelScale);
        drawTextWithShadow(batch, String.valueOf(score), centerX, topNumY, numberScale);

        // BOTTOM LEFT: High score
        drawTextWithShadow(batch, "HIGH SCORE", leftX, bottomLabelY, labelScale);
        drawTextWithShadow(batch, String.valueOf(ProgressTracker.getHighScore()), leftX, bottomNumY, numberScale);

        // BOTTOM RIGHT: Creatures Hit
        drawTextWithShadow(batch, "CREATURES HIT", rightX, bottomLabelY, labelScale);
        drawTextWithShadow(batch, String.valueOf(creaturesHit), rightX, bottomNumY, numberScale);

        // 5. Draw Buttons
        float btnWidth = panelWidth * 0.95f;
        float btnHeight = btnWidth * (playAgainButton.getHeight() / (float) playAgainButton.getWidth());

        float btnSpacing = panelWidth * 0.02f;

        float btnY = panelY - (btnHeight * 0.35f);

        // Play Again button
        float paX = centerX - btnWidth - btnSpacing / 2f;
        batch.draw(playAgainButton, paX, btnY, btnWidth, btnHeight);
        playAgainBounds.set(paX, btnY, btnWidth, btnHeight);

        // Menu button
        float mX = centerX + btnSpacing / 2f;
        batch.draw(menuButton, mX, btnY, btnWidth, btnHeight);
        menuBounds.set(mX, btnY, btnWidth, btnHeight);

        handleInput();
    }

    /**
     * Helper method to centralize text scaling, centering, and drawing drop-shadows.
     */
    private void drawTextWithShadow(SpriteBatch batch, String text, float xCenter, float y, float scale) {
        font.getData().setScale(scale);
        layout.setText(font, text);

        // Draw Shadow
        font.setColor(0, 0, 0, 0.5f);
        font.draw(batch, text, (xCenter - layout.width / 2f) + 2, y - 2);

        // Draw White Text
        font.setColor(1, 1, 1, 1);
        font.draw(batch, text, xCenter - layout.width / 2f, y);
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

        // Automatically reset the animation timer when the user leaves the game over screen!
        if (action != 0) {
            resetAnimation();
        }

        return action;
    }

    public void resetAnimation() {
        stateTime = 0f;
    }

    public void dispose() {
        if (background != null) background.dispose();
        if (title != null) title.dispose();
        if (playAgainButton != null) playAgainButton.dispose();
        if (menuButton != null) menuButton.dispose();
        if (font != null) font.dispose();
        if (overlay != null) overlay.dispose();
    }
}
