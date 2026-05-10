package com.bouncefish.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.bouncefish.leaderboard.LeaderboardData;
import com.bouncefish.leaderboard.LeaderboardService;
import com.bouncefish.utils.ColorHelper;
import com.bouncefish.utils.GameConstants;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardScreen {

    private static final int MAX_ROWS = 10;

    private Texture defaultPixmapTexture;
    private BitmapFont titleFont;
    private BitmapFont rowFont;
    private GlyphLayout glyphLayout; // we need to use to measure string width bc we can't rly know how long the string is without also font information
    private Rectangle backButtonBounds;

    private List<LeaderboardData> leaderboardScoreList;
    private boolean isLoading;

    public LeaderboardScreen() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888); // this is a default pixmap for drawing basic shapes
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fill();
        this.defaultPixmapTexture = new Texture(pixmap);
        pixmap.dispose(); // we can dispose the pixmap now that it's been loaded into a texture

        this.titleFont = new BitmapFont();
        this.rowFont = new BitmapFont();
        this.glyphLayout = new GlyphLayout();
        this.backButtonBounds = new Rectangle();
        this.leaderboardScoreList = new ArrayList<>();
    }

    public void refresh(LeaderboardService service) {
        if (service == null) {
            return;
        }
        this.isLoading = true;
        this.leaderboardScoreList.clear();
        service.fetchTopScores(new LeaderboardService.Callback() {
            @Override
            public void onDataRetrieved(List<LeaderboardData> scores) {
                if (scores != null){
                    leaderboardScoreList = scores;
                }
                else {
                    leaderboardScoreList = new ArrayList<>();
                }

                isLoading = false;
            }

            @Override
            public void onError(Exception e) {
                isLoading = false;
            }
        });
    }

    public void render(SpriteBatch batch) {
        float screenWidth = GameConstants.Game_Width;
        float screenHeight = GameConstants.Screen_Height;

        float panelWidth = screenWidth * 0.6f;
        float rowHeight = screenHeight * 0.07f;
        float titleAreaHeight = screenHeight * 0.1f;
        float backBtnHeight = rowHeight * 0.85f;
        float backBtnWidth = panelWidth * 0.3f;
        float padding = screenHeight * 0.03f;

        float panelHeight = padding + titleAreaHeight + (rowHeight * MAX_ROWS) + padding + backBtnHeight + padding;
        float panelX = (screenWidth - panelWidth) / 2f;
        float panelY = (screenHeight - panelHeight) / 2f;

        this.titleFont.getData().setScale(titleAreaHeight * 0.6f / 15f);
        this.rowFont.getData().setScale(rowHeight * 0.55f / 15f);

        batch.begin();

        // First render background
        batch.setColor(ColorHelper.LEADERBOARD_BACKGROUND);
        batch.draw(defaultPixmapTexture, panelX, panelY, panelWidth, panelHeight);

        // then use the pixmap to draw the border
        float border = 2f;
        batch.setColor(ColorHelper.LEADERBOARD_BORDER);
        batch.draw(defaultPixmapTexture, panelX - border, panelY - border, panelWidth + border * 2, border);
        batch.draw(defaultPixmapTexture, panelX - border, panelY + panelHeight, panelWidth + border * 2, border);
        batch.draw(defaultPixmapTexture, panelX - border, panelY - border, border, panelHeight + border * 2);
        batch.draw(defaultPixmapTexture, panelX + panelWidth, panelY - border, border, panelHeight + border * 2);

        // Title
        this.glyphLayout.setText(titleFont, "LEADERBOARD");
        this.titleFont.setColor(0.4f, 0.8f, 1f, 1f);
        this.titleFont.draw(batch, "LEADERBOARD",
            panelX + (panelWidth - glyphLayout.width) / 2f,
            panelY + panelHeight - padding);

        // Rows
        float rowsStartY = panelY + panelHeight - padding - titleAreaHeight;
        float rowPadX = panelX + padding;
        float rowContentWidth = panelWidth - padding * 2;

        for (int i = 0; i < MAX_ROWS; i++) {
            float rowTop = rowsStartY - i * rowHeight;

            // Alternating row background
            if (i % 2 == 0){
                batch.setColor(ColorHelper.LEADERBOARD_ROW_EVEN);
            }
            else {
                batch.setColor(ColorHelper.LEADERBOARD_ROW_ODD);
            }
            batch.draw(defaultPixmapTexture, rowPadX, rowTop - rowHeight, rowContentWidth, rowHeight);

            // Gold / silver / bronze for top 3, plain white otherwise
            if (i == 0) {
                rowFont.setColor(ColorHelper.RANK_GOLD);
            }
            else if (i == 1) {
                rowFont.setColor(ColorHelper.RANK_SILVER);
            }
            else if (i == 2) {
                rowFont.setColor(ColorHelper.RANK_BRONZE);
            }
            else {
                rowFont.setColor(ColorHelper.DEFAULT_TEXT_OFFWHITE);
            }

            float textY = rowTop - (rowHeight - rowFont.getCapHeight()) / 2f;

            String rankIndex = (i + 1) + ".";
            String name;
            String score;

            // Render loading if still fetching from firebase
            if (isLoading) {
                name = i == 0 ? "Loading..." : "";
                score = "";
            } else if (i < leaderboardScoreList.size()) {
                name = leaderboardScoreList.get(i).name;
                score = String.valueOf(leaderboardScoreList.get(i).score);
            } else {
                name = "---";
                score = "---";
            }

            glyphLayout.setText(rowFont, rankIndex);
            rowFont.draw(batch, rankIndex, rowPadX, textY);

            float nameX = rowPadX + glyphLayout.width + padding * 0.5f;
            rowFont.draw(batch, name, nameX, textY);

            glyphLayout.setText(rowFont, score);
            rowFont.draw(batch, score, rowPadX + rowContentWidth - glyphLayout.width, textY);
        }

        // Back button
        float backBtnX = panelX + (panelWidth - backBtnWidth) / 2f;
        float backBtnY = panelY + padding;
        batch.setColor(0.15f, 0.35f, 0.6f, 0.9f);
        batch.draw(defaultPixmapTexture, backBtnX, backBtnY, backBtnWidth, backBtnHeight);

        batch.setColor(1f, 1f, 1f, 1f);
        this.glyphLayout.setText(rowFont, "BACK");
        this.rowFont.draw(batch, "BACK",
            backBtnX + (backBtnWidth - this.glyphLayout.width) / 2f,
            backBtnY + (backBtnHeight + this.rowFont.getCapHeight()) / 2f);

        this.backButtonBounds.set(backBtnX, backBtnY, backBtnWidth, backBtnHeight);

        batch.end();
    }

    public boolean isBackPressed() {
        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.graphics.getHeight() - Gdx.input.getY();
            return backButtonBounds.contains(x, y);
        }
        return false;
    }

    public void dispose() {
        defaultPixmapTexture.dispose();
        titleFont.dispose();
        rowFont.dispose();
    }
}
