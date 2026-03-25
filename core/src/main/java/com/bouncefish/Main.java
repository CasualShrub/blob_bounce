package com.bouncefish;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.bouncefish.entities.BounceFish;
import com.bouncefish.entities.Creature;
import com.bouncefish.utils.GameConstants;

import java.util.ArrayList;

public class Main extends ApplicationAdapter {
    private SpriteBatch _batch;
    private Texture _image;
    private Texture _image2;
    private Texture _crabImagePlaceholder;
    private Texture _bounceFishSprite;
    private BounceGame _bounceGame;
    private BounceFish _currentFish;

    @Override
    public void create() {
        _batch = new SpriteBatch();
        _image = new Texture("blob1.png");
        _image2 = new Texture("blob2.png");
        _crabImagePlaceholder = new Texture("crab1.png");
        _bounceGame = new BounceGame();

        OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        if (_currentFish == null){
            _currentFish = _bounceGame.getBounceFish();
        }

        _bounceGame.timeStep();
        _currentFish = _bounceGame.getBounceFish();

        _batch.begin();
        if (_currentFish.isBouncing()){
            _bounceFishSprite = _image2;
        }
        else {
            _bounceFishSprite = _image;
        }
        _batch.draw(_bounceFishSprite, _currentFish.getX(), _currentFish.getY(), _currentFish.getWidth(), _currentFish.getHeight());

        ArrayList<Creature> creatureList = _bounceGame.getCreatureList();

        for (Creature creature:creatureList) {
            _batch.draw(_crabImagePlaceholder, creature.getX(), creature.getY(), creature.getWidth(), creature.getHeight());
        }
        _batch.end();
    }

    @Override
    public void dispose() {
        _batch.dispose();
        _image.dispose();
        _image2.dispose();
        _bounceFishSprite.dispose();
        _crabImagePlaceholder.dispose();;
    }
}
