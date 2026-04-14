package com.bouncefish.ui;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
public class GameOverScreen {

    private BitmapFont font;

    public GameOverScreen() {
        font = new BitmapFont();
    }

    public void render(SpriteBatch batch, int score) {

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        // Background overlay (optional dark effect)
        // You can add texture later
        batch.begin();

        font.draw(batch, "GAME OVER", screenWidth * 0.35f, screenHeight * 0.7f);
        font.draw(batch, "Score: " + score, screenWidth * 0.4f, screenHeight * 0.55f);
        font.draw(batch, "Tap to return", screenWidth * 0.35f, screenHeight * 0.4f);

        batch.end();
    }

    public boolean isTouched() {
        return Gdx.input.justTouched();
    }

}
