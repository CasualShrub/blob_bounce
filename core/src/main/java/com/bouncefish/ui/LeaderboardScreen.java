package com.bouncefish.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.bouncefish.leaderboard.LeaderboardData;
import com.bouncefish.leaderboard.LeaderboardService;

import java.util.List;

public class LeaderboardScreen extends ScreenAdapter {

    private LeaderboardService service;
    private Texture background;
    private Texture backButton;
    private BitmapFont font;

    private Table table;
    private Label statusLabel;

    public LeaderboardScreen(LeaderboardService service) {
        background = new Texture("leaderboard_bg.png");
        backButton = new Texture("back_button.png");
        font = new BitmapFont();

        Stage stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        statusLabel = new Label("Loading Scores...", new Skin());
        this.service = service;
        fetchScores(service);
    }

    private void fetchScores(LeaderboardService service){
        service.fetchTopScores(new LeaderboardService.Callback() {
            @Override
            public void onDataRetrieved(List<LeaderboardData> scores) {
                //updateUI(scores);
            }

            @Override
            public void onError(Exception e) {
                System.out.println("BRUH!");
            }
        });
    }

    private void updateUI(){

    }

    public void render(SpriteBatch batch, int score) {

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        batch.begin();

        batch.draw(background, 0, 0, screenWidth, screenHeight);

        // Title
        font.draw(batch, "LEADERBOARD", screenWidth / 2f - 60, screenHeight - 50);

        // Score display
        font.draw(batch, "Score: " + score, screenWidth / 2f - 40, screenHeight - 120);

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
