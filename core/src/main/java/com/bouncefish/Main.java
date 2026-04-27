package com.bouncefish;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.bouncefish.entities.BounceFish;
import com.bouncefish.entities.Crab;
import com.bouncefish.entities.Creature;
import com.bouncefish.entities.JellyFish;
import com.bouncefish.entities.Mackerel;
import com.bouncefish.entities.OceanSunfish;
import com.bouncefish.entities.Shark;
import com.bouncefish.entities.Water;
import com.bouncefish.gameplay.BounceGame;
import com.bouncefish.gameplay.GameState;
import com.bouncefish.gameplay.GameStateHandler;
import com.bouncefish.gameplay.ProgressTracker;
import com.bouncefish.gameplay.SoundManager;

import com.bouncefish.utils.GameConstants;
import com.bouncefish.ui.MenuScreen;
import com.bouncefish.ui.LeaderboardScreen;
import com.bouncefish.ui.GameOverScreen;

import java.util.ArrayList;

public class Main extends ApplicationAdapter {
    private SpriteBatch _batch;
    private Texture _background;
    private Texture _crabImagePlaceholder;
    private Texture _bounceFishSprite;
    private Texture _water;
    private BounceGame _bounceGame;
    private SoundManager _soundManager;
    private BounceFish _currentFish;

    private ShapeRenderer _shapeRenderer;
    private Stage _stage;
    private BitmapFont _scoreFont;
    private MenuScreen menuScreen;
    private LeaderboardScreen leaderboardScreen;
    private GameOverScreen gameOverScreen;


    @Override
    public void create() {
        _batch = new SpriteBatch();
        _shapeRenderer = new ShapeRenderer();
        _crabImagePlaceholder = new Texture("creatures/crab/crab1.png");
        _background = new Texture("background_placeholder.jpg");
        _water = new Texture("water.png");
        _soundManager = new SoundManager();
        _bounceGame = new BounceGame();
        _currentFish = _bounceGame.getBounceFish();
        menuScreen = new MenuScreen();
        leaderboardScreen = new LeaderboardScreen();
        gameOverScreen = new GameOverScreen();
        GameStateHandler.setCurrentState(GameState.MENU);

        _scoreFont = new BitmapFont();
        _scoreFont.getData().setScale(4f);
        _scoreFont.setColor(Color.WHITE);

        JellyFish.initAnime();
        Crab.initAnime();
        Mackerel.initAnime();
        Water.initAnime();
        OceanSunfish.initAnime();
        Shark.initAnime();
        // UI Elements
        _stage = new Stage();
        Gdx.input.setInputProcessor(_stage);
        Skin skin = new Skin(Gdx.files.internal("clean-crispy-ui.json"));
        TextButton debugToggleButton = new TextButton("DEBUG", skin);
        debugToggleButton.setBounds(100, Gdx.graphics.getHeight() * 0.5f, 100, 100);
        _stage.addActor(debugToggleButton);
        debugToggleButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toggleDebug();
            }
        });

        TextButton godModeToggle = new TextButton("Immortality", skin);
        godModeToggle.setBounds(100, (Gdx.graphics.getHeight() * 0.5f) - 150, 100, 100);
        _stage.addActor(godModeToggle);
        godModeToggle.addListener(new ChangeListener(){
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                toggleImmortality();
            }
        });

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(_stage);
        multiplexer.addProcessor(new GestureDetector(_bounceGame));
        Gdx.input.setInputProcessor(multiplexer);
        //OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void resetGame(){
        ProgressTracker.reset();

        // maybe tell the spawner to stop spawning crabs for now?

        // reset fish position to the start, and pause his movement.
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        if (_currentFish == null){
            return;
        }

        GameState currentState = GameStateHandler.getCurrentState();

        ArrayList<Creature> creatureList = _bounceGame.getCreatureList();

        // Only update logic if NOT in Game Over state
        if (currentState != GameState.GAME_OVER) {
            _bounceGame.timeStep();
        }

        _batch.begin();
        _batch.draw(_background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        _batch.end();

        if (GameConstants.IS_DEBUG){
            _shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            _shapeRenderer.setAutoShapeType(true);
            _shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
            _shapeRenderer.setColor(new Color(0xff000022));
            for (Creature creature:creatureList) {
                Rectangle bounds = creature.getBounds();
                _shapeRenderer.rect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
            }
            Creature player = _bounceGame.getBounceFish();
            Rectangle hitbox = player.getBounds();
            _shapeRenderer.setColor(new Color(0x00ff0022));
            _shapeRenderer.rect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());

            _shapeRenderer.setColor(new Color(0x0000ff22));

            _shapeRenderer.rectLine(GameConstants.LEFT_CONTROL_BORDER, Gdx.graphics.getHeight(), GameConstants.LEFT_CONTROL_BORDER, 0, 10f);
            _shapeRenderer.rectLine(Gdx.graphics.getWidth()-GameConstants.RIGHT_CONTROL_BORDER, Gdx.graphics.getHeight(), Gdx.graphics.getWidth()-GameConstants.RIGHT_CONTROL_BORDER, 0, 10f);

            _shapeRenderer.end();
        }

        _batch.begin(); // START RENDERING IN-GAME ENTITIES

        if (currentState == GameState.PLAYING || currentState == GameState.GAME_OVER){
            _batch.draw(_currentFish.getAnimeFrame(), _currentFish.getX(), _currentFish.getY(), _currentFish.getWidth(), _currentFish.getHeight());

            for (Creature creature:creatureList) {
                TextureRegion currentFrame = creature.getAnimeFrame();
                if(!creature.isMovingLeft()){//assuming all creature textures are facing right
                    _batch.draw(currentFrame, creature.getX(), creature.getY(), creature.getWidth(), creature.getHeight());
                }else{// negative width flips horizontally
                    _batch.draw(currentFrame, creature.getX() + creature.getWidth(), creature.getY(), -creature.getWidth(), creature.getHeight());
                }

            }

            TextureRegion waterFrame = Water.getAnimeFrame();
            _batch.draw(waterFrame, 0, -50, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            _scoreFont.draw(_batch, String.valueOf(ProgressTracker.getScore()), Gdx.graphics.getWidth() / 2f - 20, Gdx.graphics.getHeight() - 100);
        }

        _batch.end(); // END RENDERING IN-GAME ENTITIES


        // START RENDERING UI

        if (currentState == GameState.MENU){
            menuScreen.render(_batch);
            int button = menuScreen.getButtonPressed();

           if(button == 1){
               GameStateHandler.setCurrentState(GameState.PLAYING);
               _bounceGame.startGame();
           } else if(button == 2){
               GameStateHandler.setCurrentState(GameState.LEADERBOARD);
           }
        }
        else if (currentState == GameState.PLAYING){
            // Draw in game UI
        }

        else if(currentState == GameState.LEADERBOARD){
            leaderboardScreen.render(_batch, ProgressTracker.getScore());

            if(leaderboardScreen.isBackPressed()){
                GameStateHandler.setCurrentState(GameState.MENU);
            }
        }
        else if(currentState == GameState.GAME_OVER){
            // WE DON'T END BATCH HERE. We let the game render below, then overlay the UI.
            _batch.begin();
            gameOverScreen.render(_batch, ProgressTracker.getScore());
            _batch.end();

            if(gameOverScreen.isTouched()){
                resetGame();
                GameStateHandler.setCurrentState(GameState.MENU);
            }
        }

        // END RENDERING UI

        _stage.draw();
    }

    @Override
    public void dispose() {
        _batch.dispose();
        _bounceFishSprite.dispose();
        _crabImagePlaceholder.dispose();
        _shapeRenderer.dispose();
        _soundManager.disposeSounds();
        menuScreen.dispose();
        _scoreFont.dispose();
    }

    private void toggleDebug(){
        GameConstants.IS_DEBUG = !GameConstants.IS_DEBUG;
    }
    private void toggleImmortality(){
        GameConstants.IS_IMMORTAL = !GameConstants.IS_IMMORTAL;
    }
}
