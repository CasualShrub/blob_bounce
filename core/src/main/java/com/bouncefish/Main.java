package com.bouncefish;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Matrix4;
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
import com.bouncefish.gameplay.BounceGame;
import com.bouncefish.gameplay.SoundManager;
import com.bouncefish.utils.GameConstants;
import com.bouncefish.ui.MenuScreen;

import java.util.ArrayList;

public class Main extends ApplicationAdapter {
    private SpriteBatch _batch;
    private Texture _image;
    private Texture _image2;
    private Texture _image3;
    private Texture _background;
    private Texture _crabImagePlaceholder;
    private Texture _bounceFishSprite;
    private Texture _water;
    private BounceGame _bounceGame;
    private SoundManager _soundManager;
    private BounceFish _currentFish;

    private ShapeRenderer _shapeRenderer;
    private Stage _stage;

    private enum GameState{
        MENU,
        PLAYING
    }

    private GameState currentState;
    private MenuScreen menuScreen;

    @Override
    public void create() {
        _batch = new SpriteBatch();
        _shapeRenderer = new ShapeRenderer();
        _image = new Texture("blob1.png");
        _image2 = new Texture("blob2.png");
        _image3 = new Texture("blob3.png");
        _crabImagePlaceholder = new Texture("crab1.png");
        _background = new Texture("background_placeholder.jpg");
        _water = new Texture("water.png");
        _soundManager = new SoundManager();
        _bounceGame = new BounceGame();
        menuScreen = new MenuScreen();
        currentState = GameState.MENU;

        JellyFish.initAnime();
        Crab.initAnime();
        Mackerel.initAnime();
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

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        if (_currentFish == null){
            _currentFish = _bounceGame.getBounceFish();
        }

        ArrayList<Creature> creatureList = _bounceGame.getCreatureList();

        _bounceGame.timeStep();
        _currentFish = _bounceGame.getBounceFish();

        _batch.begin();
        _batch.draw(_background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        _batch.end();

        if (GameConstants.IS_DEBUG){
            _shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            _shapeRenderer.setAutoShapeType(true);
            _shapeRenderer.set(ShapeRenderer.ShapeType.Filled);
            _shapeRenderer.setColor(new Color(0xff000022));
            for (Creature creature:creatureList) {
                _shapeRenderer.rect(creature.getX(), creature.getY(), creature.getWidth(), creature.getHeight());
            }
            Creature player = _bounceGame.getBounceFish();
            _shapeRenderer.setColor(new Color(0x00ff0022));
            _shapeRenderer.rect(player.getX(), player.getY(), player.getWidth(), player.getHeight());

            _shapeRenderer.setColor(new Color(0x0000ff22));


            _shapeRenderer.rectLine(GameConstants.LEFT_CONTROL_BORDER, Gdx.graphics.getHeight(), GameConstants.LEFT_CONTROL_BORDER, 0, 10f);
            _shapeRenderer.rectLine(Gdx.graphics.getWidth()-GameConstants.RIGHT_CONTROL_BORDER, Gdx.graphics.getHeight(), Gdx.graphics.getWidth()-GameConstants.RIGHT_CONTROL_BORDER, 0, 10f);


            _shapeRenderer.end();
        }

        _batch.begin(); // START RENDERING IN-GAME ENTITIES

        if (currentState == GameState.PLAYING){
            if (_currentFish.isDead()){
                _bounceFishSprite = _image3;
            }
            else if (_currentFish.isBouncing()){
                _bounceFishSprite = _image2;
            }
            else {
                _bounceFishSprite = _image;
            }
            _batch.draw(_bounceFishSprite, _currentFish.getX(), _currentFish.getY(), _currentFish.getWidth(), _currentFish.getHeight());

            for (Creature creature:creatureList) {
                TextureRegion currentFrame = creature.getAnimeFrame();
                //if (creature.isRotated()){
                if (false){ //optional feature that rotates the texture to face the direction of speed
                    Matrix4 rotationMatrix = creature.getTextureRotationMatrix();
                    _batch.setTransformMatrix(rotationMatrix);
                    _batch.draw(currentFrame, creature.getX(), creature.getY(), creature.getWidth(), creature.getHeight());
                    _batch.setTransformMatrix(new Matrix4());
                    //TODO: should check whether the fish is going right or left and flip the texture if necessary
                } else {
                    _batch.draw(currentFrame, creature.getX(), creature.getY(), creature.getWidth(), creature.getHeight());
                }
            }

            _batch.draw(_water, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        _batch.end(); // END RENDERING IN-GAME ENTITIES

        _batch.begin(); // START RENDERING UI

        if (currentState == GameState.MENU){
            menuScreen.render(_batch);
            if(menuScreen.isPlayPressed()){
                currentState = GameState.PLAYING;
                _bounceGame.startGame();
            }
        }
        else if (currentState == GameState.PLAYING){
            // Draw in game UI
        }

        _batch.end(); // END RENDERING UI

        _stage.draw();
    }

    @Override
    public void dispose() {
        _batch.dispose();
        _image.dispose();
        _image2.dispose();
        _image3.dispose();
        _bounceFishSprite.dispose();
        _crabImagePlaceholder.dispose();
        _shapeRenderer.dispose();
        _soundManager.disposeSounds();
        menuScreen.dispose();
    }

    private void toggleDebug(){
        GameConstants.IS_DEBUG = !GameConstants.IS_DEBUG;
    }
    private void toggleImmortality(){
        GameConstants.IS_IMMORTAL = !GameConstants.IS_IMMORTAL;
    }
}
