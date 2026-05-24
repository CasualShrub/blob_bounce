package com.bouncefish.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.bouncefish.gameplay.ProgressTracker;

public class GameOverScreen {

    // Only one texture asset: the "GAME OVER" title that bounces in
    private Texture title;
    // 1x1 white pixel used to draw all coloured rectangles
    private Texture pixel;

    private BitmapFont labelFont;  // category labels (YOUR SCORE etc.)
    private BitmapFont buttonFont; // button text — slightly smaller than labels
    private BitmapFont numberFont; // large score values
    private GlyphLayout layout;

    private Rectangle playAgainBounds;
    private Rectangle menuBounds;

    private int actionPressed = 0; // 0: none, 1: play again, 2: menu
    private float stateTime = 0f;

    public GameOverScreen() {
        title = new Texture("GameoverScreen/gameovertitle.png");

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        pixel = new Texture(pixmap);
        pixmap.dispose();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Medium.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.minFilter = Texture.TextureFilter.Linear;
        params.magFilter = Texture.TextureFilter.Linear;

        int labelPx = Math.max(12, (int)(Gdx.graphics.getWidth() / 62f));
        params.size = labelPx;
        labelFont = generator.generateFont(params);
        labelFont.setUseIntegerPositions(false);

        params.size = Math.max(10, (int)(labelPx * 0.65f));
        buttonFont = generator.generateFont(params);
        buttonFont.setUseIntegerPositions(false);

        params.size = Math.max(28, (int)(Gdx.graphics.getWidth() / 32f));
        numberFont = generator.generateFont(params);
        numberFont.setUseIntegerPositions(false);

        generator.dispose();
        layout = new GlyphLayout();

        playAgainBounds = new Rectangle();
        menuBounds = new Rectangle();
    }

    public void render(SpriteBatch batch, int score, int creaturesHit) {
        stateTime += Gdx.graphics.getDeltaTime();

        float sw = Gdx.graphics.getWidth();
        float sh = Gdx.graphics.getHeight();

        batch.setColor(0f, 0f, 0f, 0.32f);
        batch.draw(pixel, 0, 0, sw, sh);
        batch.setColor(1, 1, 1, 1);

        float panelH = sh * 0.50f;
        float panelW = Math.min(panelH * 1.4f, sw * 0.88f);
        if (panelW < panelH * 1.4f) panelH = panelW / 1.4f;
        float panelX  = (sw - panelW) / 2f;
        float panelY  = (sh - panelH) / 2f - sh * 0.10f;
        float panelTopY = panelY + panelH;

        float titleW  = sw * 0.32f;
        float titleH  = titleW * (title.getHeight() / (float) title.getWidth() * 0.7f);
        float spaceAbove  = sh - panelTopY;
        float targetTitleY = panelTopY + (spaceAbove - titleH) * 0.5f;
        float startTitleY  = sh * 1.2f;
        float animProgress = Math.min(1f, stateTime / 1.2f);
        float titleY = startTitleY + (targetTitleY - startTitleY) * Interpolation.bounceOut.apply(animProgress);
        batch.draw(title, (sw - titleW) / 2f, titleY, titleW, titleH);

        float border = 1.5f;
        batch.setColor(0.22f, 0.52f, 0.82f, 0.45f);
        batch.draw(pixel, panelX - border, panelY - border, panelW + border * 2f, panelH + border * 2f);

        batch.setColor(0.05f, 0.08f, 0.17f, 0.97f);
        batch.draw(pixel, panelX, panelY, panelW, panelH);

        float accentH = Math.max(3f, panelH * 0.024f);
        batch.setColor(0.0f, 0.78f, 0.96f, 1.0f);
        batch.draw(pixel, panelX, panelTopY - accentH, panelW, accentH);

        float hPad = panelW * 0.04f;
        float btnH = panelH * 0.17f;
        float btnGap = panelW * 0.03f;
        float btnW = (panelW - hPad * 2f - btnGap) / 2f;
        float btnY = panelY + panelH * 0.05f;
        float btnLX = panelX + hPad;
        float btnRX = btnLX + btnW + btnGap;

        float botSecH = panelH * 0.26f;
        float botSecY = btnY + btnH + panelH * 0.03f;

        float dividerY = botSecY + botSecH;

        float topSecY  = dividerY + 2f;
        float topSecH  = (panelTopY - accentH) - topSecY;

        // Divider
        batch.setColor(0.25f, 0.45f, 0.65f, 0.35f);
        batch.draw(pixel, panelX + hPad, dividerY, panelW - hPad * 2f, 1.5f);


        batch.setColor(0.0f, 0.55f, 0.75f, 1.0f);
        batch.draw(pixel, btnLX, btnY, btnW, btnH);
        batch.setColor(1f, 1f, 1f, 0.10f);
        batch.draw(pixel, btnLX, btnY + btnH * 0.75f, btnW, btnH * 0.25f);

        batch.setColor(0.12f, 0.20f, 0.38f, 1.0f);
        batch.draw(pixel, btnRX, btnY, btnW, btnH);
        batch.setColor(1f, 1f, 1f, 0.08f);
        batch.draw(pixel, btnRX, btnY + btnH * 0.75f, btnW, btnH * 0.25f);

        playAgainBounds.set(btnLX, btnY, btnW, btnH);
        menuBounds.set(btnRX, btnY, btnW, btnH);

        float btnTextY = btnY + (btnH + labelFont.getCapHeight()) / 2f;
        drawCentered(batch, "PLAY AGAIN",btnLX + btnW / 2f, btnTextY, buttonFont, 1f, 1f, 1f, 1f);
        drawCentered(batch, "MENU",btnRX + btnW / 2f, btnTextY, buttonFont, 1f, 1f, 1f, 1f);

        float topCX    = panelX + panelW / 2f;
        float topLblY  = topSecY + topSecH * 0.82f;
        float topValY  = topSecY + topSecH * 0.44f;
        drawCentered(batch, "YOUR SCORE", topCX, topLblY, labelFont,  0.55f, 0.80f, 1.00f, 1f);
        drawCentered(batch, String.valueOf(score), topCX, topValY, numberFont, 1f, 1f, 1f, 1f);

        float colL  = panelX + panelW * 0.26f;
        float colR  = panelX + panelW * 0.74f;
        float bLblY = botSecY + botSecH * 0.92f;
        float bValY = botSecY + botSecH * 0.55f;
        drawCentered(batch, "HIGH SCORE",                              colL, bLblY, labelFont,  0.55f, 0.80f, 1.00f, 1f);
        drawCentered(batch, String.valueOf(ProgressTracker.getHighScore()), colL, bValY, numberFont, 1f, 1f, 1f, 1f);
        drawCentered(batch, "CREATURES HIT",                           colR, bLblY, labelFont,  0.55f, 0.80f, 1.00f, 1f);
        drawCentered(batch, String.valueOf(creaturesHit),              colR, bValY, numberFont, 1f, 1f, 1f, 1f);

        handleInput();
    }

    private void drawCentered(SpriteBatch batch, String text, float cx, float y,
                               BitmapFont f, float r, float g, float b, float a) {
        layout.setText(f, text);
        float x = cx - layout.width / 2f;

        f.setColor(0f, 0f, 0f, a * 0.45f);
        f.draw(batch, text, x + 2f, y - 2f);

        f.setColor(r, g, b, a);
        f.draw(batch, text, x, y);
    }

    private void handleInput() {
        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY();
            if (playAgainBounds.contains(x, y)) actionPressed = 1;
            else if (menuBounds.contains(x, y))  actionPressed = 2;
        }
    }

    public int getActionPressed() {
        int action = actionPressed;
        actionPressed = 0;
        if (action != 0) resetAnimation();
        return action;
    }

    public void resetAnimation() {
        stateTime = 0f;
    }

    public void dispose() {
        if (title != null) title.dispose();
        if (pixel != null) pixel.dispose();
        if (labelFont != null)  labelFont.dispose();
        if (buttonFont != null) buttonFont.dispose();
        if (numberFont != null) numberFont.dispose();
    }
}
