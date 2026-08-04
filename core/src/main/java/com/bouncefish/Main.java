package com.bouncefish;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.bouncefish.entities.BounceFish;
import com.bouncefish.entities.Crab;
import com.bouncefish.entities.Creature;
import com.bouncefish.entities.JellyFish;
import com.bouncefish.entities.Mackerel;
import com.bouncefish.entities.Marlin;
import com.bouncefish.entities.OceanSunfish;
import com.bouncefish.entities.Shark;
import com.bouncefish.entities.Water;
import com.bouncefish.gameplay.BounceGame;
import com.bouncefish.gameplay.GameState;
import com.bouncefish.gameplay.GameStateHandler;
import com.bouncefish.gameplay.PowerUpType;
import com.bouncefish.gameplay.ProgressTracker;
import com.bouncefish.gameplay.SoundManager;

import com.bouncefish.leaderboard.LeaderboardService;
import com.bouncefish.utils.ColorHelper;
import com.bouncefish.utils.GameConstants;
import com.bouncefish.ui.MenuScreen;
import com.bouncefish.ui.LeaderboardScreen;
import com.bouncefish.ui.GameOverScreen;

import java.util.ArrayList;

public class Main extends ApplicationAdapter {
    private SpriteBatch _batch;
    private Texture _background;
    private Texture _crabImagePlaceholder;
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

    private Texture _pixel;
    private BitmapFont _powerUpFont;
    private Rectangle _powerUpBounds;

    private int lastScore = 0;
    private float _scoreBounceTimer = 0f;
    private float _tutorialTimer = 0f;
    private static final float SCORE_POP_DURATION = 0.3f;
    private static final float SCORE_POP_MAX_SCALE = 1.5f;

    private LeaderboardService leaderboardService;

    public Main(LeaderboardService service){
        this.leaderboardService = service;
    }

    @Override
    public void create() {
        _batch = new SpriteBatch();
        _shapeRenderer = new ShapeRenderer();
        _crabImagePlaceholder = new Texture("creatures/crab/crab1.png");
        _background = new Texture("background_placeholder1.jpg");
        _water = new Texture("water.png");
        _soundManager = new SoundManager();
        _bounceGame = new BounceGame(leaderboardService);
        _currentFish = _bounceGame.getBounceFish();
        menuScreen = new MenuScreen();
        leaderboardScreen = new LeaderboardScreen();
        gameOverScreen = new GameOverScreen();
        GameStateHandler.setCurrentState(GameState.MENU);

        FreeTypeFontGenerator scoreFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("Roboto-Medium.ttf"));
        FreeTypeFontParameter scoreFontParams = new FreeTypeFontParameter();
        scoreFontParams.minFilter = Texture.TextureFilter.Linear;
        scoreFontParams.magFilter = Texture.TextureFilter.Linear;
        scoreFontParams.color = Color.WHITE;

        scoreFontParams.size = 72;
        _scoreFont = scoreFontGenerator.generateFont(scoreFontParams);
        _scoreFont.setUseIntegerPositions(false);

        scoreFontParams.size = Math.max(14, (int)(Gdx.graphics.getHeight() * 0.035f));
        _powerUpFont = scoreFontGenerator.generateFont(scoreFontParams);
        _powerUpFont.setUseIntegerPositions(false);

        scoreFontGenerator.dispose();

        Pixmap pixmap = new Pixmap(1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fill();
        _pixel = new Texture(pixmap);
        pixmap.dispose();

        _powerUpBounds = new Rectangle();

        JellyFish.initAnime();
        Crab.initAnime();
        Mackerel.initAnime();
        Water.initAnime();
        OceanSunfish.initAnime();
        Shark.initAnime();
        Marlin.initAnime();

        // UI Elements
        _stage = new Stage();
        Gdx.input.setInputProcessor(_stage);
        Skin skin = new Skin(Gdx.files.internal("clean-crispy-ui.json"));

        if (GameConstants.IS_DEBUG){
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
        }

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(_stage);
        multiplexer.addProcessor(new GestureDetector(_bounceGame));
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void resetGame(){
        ProgressTracker.reset();
        _tutorialTimer = 0f;
        _bounceGame.onMainMenu();
        _currentFish = _bounceGame.getBounceFish();

        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(_stage);
        multiplexer.addProcessor(new GestureDetector(_bounceGame));
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        if (_currentFish == null){
            return;
        }

        GameState currentState = GameStateHandler.getCurrentState();
        ArrayList<Creature> creatureList = _bounceGame.getCreatureList();

        // Update logic in both PLAYING and GAME_OVER states to keep background active
        if (currentState == GameState.PLAYING || currentState == GameState.GAME_OVER) {
            _bounceGame.timeStep();
        }
        _currentFish = _bounceGame.getBounceFish();

        _batch.begin();
        _batch.draw(_background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        _batch.end();

        if (GameConstants.IS_DEBUG && GameStateHandler.getCurrentState() == GameState.PLAYING){
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

        _batch.begin();

        if (currentState == GameState.PLAYING || currentState == GameState.GAME_OVER){
            _batch.draw(_currentFish.getAnimeFrame(), _currentFish.getX(), _currentFish.getY(), _currentFish.getWidth(), _currentFish.getHeight());

            for (Creature creature:creatureList) {
                TextureRegion currentFrame = creature.getAnimeFrame();
                float rw = creature.getRenderWidth();
                float rh = creature.getRenderHeight();
                float xOff = creature.getRenderXOffset();
                if(!creature.shouldFlipHorizontally()){
                    _batch.draw(currentFrame, creature.getX() + xOff, creature.getY(), rw, rh);
                }else{
                    _batch.draw(currentFrame, creature.getX() + xOff + rw, creature.getY(), -rw, rh);
                }
            }

            Array<Sprite> splashSprites = Water.getSplashFrames();
            for (Sprite splash : splashSprites) {
                splash.draw(_batch);
            }

            TextureRegion waterFrame = Water.getAnimeFrame();
            _batch.draw(waterFrame, 0, -50, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

            if (currentState == GameState.PLAYING) {
                int currentScore = ProgressTracker.getScore();
                if (currentScore != lastScore) {
                    // added a little pulsing animation when the user's score goes to 10
                    if (currentScore > 0 && currentScore % 10 == 0) {
                        _scoreBounceTimer = SCORE_POP_DURATION;
                    }
                    lastScore = currentScore;
                }

                // when we reach a multiple of 10 set the bounce animation timer and use sine to make it go big then go small
                if (_scoreBounceTimer > 0) {
                    _scoreBounceTimer -= Gdx.graphics.getDeltaTime();
                    float t = Math.max(0, _scoreBounceTimer) / SCORE_POP_DURATION;
                    float scale = 1f + (SCORE_POP_MAX_SCALE - 1f) * (float)Math.sin(t * Math.PI);
                    _scoreFont.getData().setScale(scale);
                } else {
                    _scoreFont.getData().setScale(1f);
                }

                if (_currentFish.isInStasis()) {
                    int secsLeft = (int) Math.ceil(_currentFish.getStasisTimeRemaining());
                    String countdownText = "Begin bouncing in " + secsLeft + "...";
                    _scoreFont.getData().setScale(0.5f);
                    _scoreFont.draw(_batch, countdownText, (Gdx.graphics.getWidth() / 2f) - 10, Gdx.graphics.getHeight() - 100, 0, Align.center, false);
                    _scoreFont.getData().setScale(1f);
                } else {
                    _scoreFont.draw(_batch, String.valueOf(ProgressTracker.getScore()), (Gdx.graphics.getWidth() / 2f) - 10, Gdx.graphics.getHeight() - 100);
                }

                float powerUpWidth = GameConstants.RIGHT_CONTROL_BORDER;
                float powerUpHeight = Gdx.graphics.getHeight() * 0.16f;
                float powerUpMargin = Gdx.graphics.getHeight() * 0.03f;
                float powerUPX = Gdx.graphics.getWidth() - powerUpWidth;
                float powerUpY = powerUpMargin;
                _powerUpBounds.set(powerUPX, powerUpY, powerUpWidth, powerUpHeight);

                _batch.setColor(ColorHelper.POWERUP_GRAY);
                _batch.draw(_pixel, powerUPX, powerUpY, powerUpWidth, powerUpHeight);

                if (_currentFish.isFloating()) {
                    // Float active — show draining gold bar (takes priority over cooldown)
                    float fillWidth = powerUpWidth * _currentFish.getFloatProgress();
                    _batch.setColor(ColorHelper.RANK_GOLD);
                    _batch.draw(_pixel, powerUPX, powerUpY, fillWidth, powerUpHeight);
                } else if (_currentFish.isOnCooldown()) {
                    // Cooldown — draining red bar
                    float fillFrac = _currentFish.getCooldownProgress();
                    _batch.setColor(0.55f, 0.15f, 0.15f, 0.85f);
                    _batch.draw(_pixel, powerUPX, powerUpY, powerUpWidth, powerUpHeight * fillFrac);
                } else if (_currentFish.hasPowerUp()) {
                    Color chargedColor = _currentFish.getPowerUpType() == PowerUpType.GROUND_POUND
                            ? ColorHelper.POWERUP_GROUND_POUND
                            : ColorHelper.RANK_GOLD;
                    _batch.setColor(chargedColor);
                    _batch.draw(_pixel, powerUPX, powerUpY, powerUpWidth, powerUpHeight);
                }

                _batch.setColor(1f, 1f, 1f, 1f);
                if (_currentFish.isFloating()) {
                    _powerUpFont.draw(_batch, _currentFish.getPowerUpLabel(), powerUPX, powerUpY + (powerUpHeight + _powerUpFont.getCapHeight()) / 2f, powerUpWidth, Align.center, false);
                } else if (_currentFish.isOnCooldown()) {
                    int secsLeft = (int) Math.ceil(_currentFish.getPowerUpCooldownRemaining());
                    _powerUpFont.draw(_batch, secsLeft + "s", powerUPX, powerUpY + (powerUpHeight + _powerUpFont.getCapHeight()) / 2f, powerUpWidth, Align.center, false);
                } else {
                    _powerUpFont.draw(_batch, _currentFish.getPowerUpLabel(), powerUPX, powerUpY + (powerUpHeight + _powerUpFont.getCapHeight()) / 2f, powerUpWidth, Align.center, false);
                }

                // A quick little tutorial! Show for a few seconds then fade out
                float tutorialSeconds = 4f;
                if (_tutorialTimer < tutorialSeconds) {
                    _tutorialTimer += Gdx.graphics.getDeltaTime();
                    float alpha = _tutorialTimer < 2f
                            ? 0.30f
                            : 0.30f * (1f - (_tutorialTimer - 2f));

                    float zoneW = GameConstants.LEFT_CONTROL_BORDER;

                    // Left zone
                    _batch.setColor(0.35f, 0.75f, 1f, alpha);
                    _batch.draw(_pixel, 0, 0, zoneW, Gdx.graphics.getHeight());

                    // Right zone
                    _batch.setColor(0.35f, 0.75f, 1f, alpha);
                    _batch.draw(_pixel, Gdx.graphics.getWidth() - zoneW, 0, zoneW, Gdx.graphics.getHeight());

                    // Labels
                    _batch.setColor(1f, 1f, 1f, Math.min(alpha * 4f, 0.85f));
                    _powerUpFont.setColor(1f, 1f, 1f, Math.min(alpha * 4f, 0.85f));
                    _powerUpFont.draw(_batch, "HOLD LEFT\nTO MOVE LEFT",  0,Gdx.graphics.getHeight() * 0.5f + _powerUpFont.getCapHeight(), zoneW, Align.center, false);
                    _powerUpFont.draw(_batch, "HOLD RIGHT\nTO MOVE RIGHT", Gdx.graphics.getWidth() - zoneW, Gdx.graphics.getHeight() * 0.5f + _powerUpFont.getCapHeight(), zoneW, Align.center, false);
                    _powerUpFont.setColor(1f, 1f, 1f, 1f);
                    _batch.setColor(1f, 1f, 1f, 1f);
                }
            }
        }
        _batch.end();

        if (currentState == GameState.PLAYING) {
            if (Gdx.input.justTouched()) {
                float tx = Gdx.input.getX();
                float ty = Gdx.graphics.getHeight() - Gdx.input.getY();
                if (_powerUpBounds.contains(tx, ty)) {
                    _currentFish.activatePowerUp();
                }
            }
            if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.SPACE)) {
                _currentFish.activatePowerUp();
            }
        }

        if (currentState == GameState.MENU){
            menuScreen.render(_batch);
            int button = menuScreen.getButtonPressed();
            if(button == 1){
                resetGame();
                GameStateHandler.setCurrentState(GameState.PLAYING);
                _bounceGame.startGame();
            } else if(button == 2){
                leaderboardScreen.refresh(leaderboardService); // update the leaderboardscreen TODO: maybe move this inside render?
                GameStateHandler.setCurrentState(GameState.LEADERBOARD);
            }
        }
        else if(currentState == GameState.LEADERBOARD){
            leaderboardScreen.render(_batch);
            if(leaderboardScreen.isBackPressed()){
                GameStateHandler.setCurrentState(GameState.MENU);
            }
        }
        else if(currentState == GameState.GAME_OVER){
            _batch.begin();
            gameOverScreen.render(_batch, ProgressTracker.getScore(), ProgressTracker.getCreaturesHit());
            _batch.end();

            int action = gameOverScreen.getActionPressed();
            if(action == 1){
                resetGame();
                GameStateHandler.setCurrentState(GameState.PLAYING);
                _bounceGame.startGame();
            } else if(action == 2){
                resetGame();
                GameStateHandler.setCurrentState(GameState.MENU);
            }
        }

        _stage.draw();
    }

    @Override
    public void dispose() {
        _batch.dispose();
        _crabImagePlaceholder.dispose();
        _shapeRenderer.dispose();
        _soundManager.disposeSounds();
        menuScreen.dispose();
        leaderboardScreen.dispose();
        _pixel.dispose();
        _powerUpFont.dispose();
        _scoreFont.dispose();
        gameOverScreen.dispose();
    }

    private void toggleDebug(){
        GameConstants.IS_DEBUG = !GameConstants.IS_DEBUG;
    }
    private void toggleImmortality(){
        GameConstants.IS_IMMORTAL = !GameConstants.IS_IMMORTAL;
    }
}
