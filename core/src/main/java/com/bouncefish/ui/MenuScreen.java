package com.bouncefish.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.bouncefish.utils.GameConstants;

public class MenuScreen {

    private Texture background;
    private Texture playButton;
    private Texture leaderboardButton;
    private Texture title;
    //private Texture settingsButton;
    private Texture defaultImagePixmap;
    private BitmapFont font;

    private Rectangle playBounds;
    private Rectangle leaderboardBounds;
    //private Rectangle settingsBounds;
    private Rectangle nameFieldBounds;
    private Rectangle saveBounds;

    private String playerName;
    private boolean nameSaved;
    private float saveButtonFlashDuration;

    public MenuScreen() {
        background = new Texture("Menuscreen/Menubackground.png");
        playButton = new Texture("Menuscreen/Play_button (3).png");
        leaderboardButton = new Texture("Menuscreen/Leaderboard_button.png");
        title = new Texture("Menuscreen/Bouncefish_title.png");
        //settingsButton = new Texture("Menuscreen/setting_button.png");

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fill();
        defaultImagePixmap = new Texture(pixmap);
        pixmap.dispose();

        font = new BitmapFont();

        playBounds = new Rectangle();
        leaderboardBounds = new Rectangle();
        //settingsBounds = new Rectangle();
        nameFieldBounds = new Rectangle();
        saveBounds = new Rectangle();

        Preferences prefs = Gdx.app.getPreferences(GameConstants.PREFS_NAME);
        playerName = prefs.getString(GameConstants.PREF_PLAYER_NAME, "");
        nameSaved = !playerName.isEmpty();
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
        float rowHeight = btnHeight * 0.30f;
        float saveBtnWidth = btnWidth * 0.25f;
        float rowGap = btnWidth * 0.03f;
        float nameFieldWidth = btnWidth - saveBtnWidth - rowGap;

        // 2. Calculating Vertical Stack (Title + Buttons + Name Row)
        float titleSpacing = btnHeight * 0.10f;
        float nameRowSpacing = btnHeight * 0.18f;
        float totalHeight = titleHeight + (btnHeight * 2) + titleSpacing + nameRowSpacing + rowHeight;
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
        currentY -= (titleSpacing + rowHeight);
        float rowX = btnX;
        float saveBtnX = rowX + nameFieldWidth + rowGap;
        float border = 2f;

        batch.setColor(0f, 0f, 0f, 0.55f);
        batch.draw(defaultImagePixmap, rowX, currentY, nameFieldWidth, rowHeight);

        //draw the borders as well using
        batch.setColor(1f, 1f, 1f, 0.6f);
        batch.draw(defaultImagePixmap, rowX - border, currentY - border, nameFieldWidth + border * 2, border);
        batch.draw(defaultImagePixmap, rowX - border, currentY + rowHeight, nameFieldWidth + border * 2, border);
        batch.draw(defaultImagePixmap, rowX - border, currentY - border, border, rowHeight + border * 2);
        batch.draw(defaultImagePixmap, rowX + nameFieldWidth, currentY - border, border, rowHeight + border * 2);

        saveButtonFlashDuration -= Gdx.graphics.getDeltaTime();
        boolean isFLashing = saveButtonFlashDuration > 0;
        batch.setColor(isFLashing ? 0.2f : 0.5f, isFLashing ? 0.55f : 1.0f, isFLashing ? 0.2f : 0.5f, 0.9f);
        batch.draw(defaultImagePixmap, saveBtnX, currentY, saveBtnWidth, rowHeight);

        // Text
        font.getData().setScale(rowHeight / 30f);
        float textY = currentY + (rowHeight + font.getCapHeight()) / 2f;

        boolean empty = playerName.isEmpty();
        font.setColor(empty ? 0.55f : 1f, empty ? 0.55f : 1f, empty ? 0.55f : 1f, 1f);
        font.draw(batch, empty ? "Enter a name to play..." : playerName, rowX + rowHeight * 0.2f, textY);

        font.setColor(1f, 1f, 1f, 1f);
        font.draw(batch, "SAVE", saveBtnX + saveBtnWidth * 0.15f, textY);

        // Reset batch color
        batch.setColor(1f, 1f, 1f, 1f);

        nameFieldBounds.set(rowX, currentY, nameFieldWidth, rowHeight);
        saveBounds.set(saveBtnX, currentY, saveBtnWidth, rowHeight);

        // Draw Buttons below Name Row
        currentY -= (nameRowSpacing + btnHeight);
        if (!nameSaved) batch.setColor(0.45f, 0.45f, 0.45f, 0.6f);
        batch.draw(playButton, btnX, currentY, btnWidth, btnHeight);
        batch.setColor(1f, 1f, 1f, 1f);
        playBounds.set(btnX, currentY, btnWidth, btnHeight);

        currentY -= (btnHeight);
        batch.draw(leaderboardButton, btnX, currentY, btnWidth, btnHeight);
        leaderboardBounds.set(btnX, currentY, btnWidth, btnHeight);

        //currentY -= (btnHeight);
        //batch.draw(settingsButton, btnX, currentY, btnWidth, btnHeight);
        //settingsBounds.set(btnX, currentY, btnWidth, btnHeight);

        batch.end();
    }

    public int getButtonPressed() {
        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY();

            if (nameSaved && playBounds.contains(x, y)) return 1;
            if (leaderboardBounds.contains(x, y)) return 2;
            //if (settingsBounds.contains(x, y)) return 3;

            if (nameFieldBounds.contains(x, y)) {
                Gdx.input.getTextInput(new Input.TextInputListener() {
                    @Override
                    public void input(String text) {
                        playerName = text.trim(); // should probably just get rid of extra white sopace
                    }
                    @Override
                    public void canceled() {}
                }, "Enter your name", playerName, "");
            }

            if (saveBounds.contains(x, y)) {
                Preferences prefs = Gdx.app.getPreferences(GameConstants.PREFS_NAME);
                prefs.putString(GameConstants.PREF_PLAYER_NAME, playerName);
                prefs.flush();
                saveButtonFlashDuration = 0.35f;
                nameSaved = !playerName.isEmpty();
            }
        }
        return 0;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void dispose() {
        background.dispose();
        playButton.dispose();
        leaderboardButton.dispose();
        title.dispose();
        //settingsButton.dispose();
        defaultImagePixmap.dispose();
        font.dispose();
    }
}
