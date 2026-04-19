package com.bouncefish.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bouncefish.Main;
import com.bouncefish.gameplay.LeaderboardService;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardScreen {

    private Texture background;
    private Texture backButton;
    private BitmapFont font;
    private List<LeaderboardService.ScoreEntry> topScores;
    private boolean isLoading = false;

    public LeaderboardScreen() {
        background = new Texture("leaderboard_bg.png");
        backButton = new Texture("back_button.png");
        font = new BitmapFont();
        topScores = new ArrayList<>();
    }

    private void fetchScores() {
        LeaderboardService service = Main.getLeaderboardService();
        if (service != null && !isLoading) {
            isLoading = true;
            service.fetchTopScores(new LeaderboardService.ScoreCallback() {
                @Override
                public void onScoresLoaded(List<LeaderboardService.ScoreEntry> scores) {
                    topScores = scores;
                    isLoading = false;
                }

                @Override
                public void onError(Exception e) {
                    isLoading = false;
                }
            });
        }
    }

    public void render(SpriteBatch batch, int currentScore) {
        if (topScores.isEmpty() && !isLoading) {
            fetchScores();
        }

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        batch.begin();
        batch.draw(background, 0, 0, screenWidth, screenHeight);

        // Title
        font.draw(batch, "LEADERBOARD - TOP 10", screenWidth / 2f - 80, screenHeight - 50);

        if (isLoading) {
            font.draw(batch, "Loading scores...", screenWidth / 2f - 50, screenHeight / 2f);
        } else {
            float yOffset = screenHeight - 120;
            for (int i = 0; i < topScores.size(); i++) {
                LeaderboardService.ScoreEntry entry = topScores.get(i);
                font.draw(batch, (i + 1) + ". " + entry.name + ": " + entry.score, screenWidth / 2f - 80, yOffset);
                yOffset -= 30;
            }
        }

        // Back button
        float btnWidth = screenWidth * 0.3f;
        float btnHeight = btnWidth * 0.4f;
        float btnX = (screenWidth - btnWidth) / 2;
        float btnY = 50;

        batch.draw(backButton, btnX, btnY, btnWidth, btnHeight);
        batch.end();
    }

    public boolean isBackPressed() {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float btnWidth = screenWidth * 0.3f;
        float btnHeight = btnWidth * 0.4f;
        float btnX = (screenWidth - btnWidth) / 2;
        float btnY = 50;

        if (Gdx.input.justTouched()) {
            float x = Gdx.input.getX();
            float y = screenHeight - Gdx.input.getY();

            if (x >= btnX && x <= btnX + btnWidth &&
                y >= btnY && y <= btnY + btnHeight) {
                // Clear scores so they refresh next time we enter
                topScores.clear();
                return true;
            }
        }
        return false;
    }

    public void dispose() {
        background.dispose();
        backButton.dispose();
        font.dispose();
    }
}
