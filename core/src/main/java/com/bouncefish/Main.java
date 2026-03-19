package com.bouncefish;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.bouncefish.entities.BounceFish;

public class Main extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture image;
    private BounceGame _bounceGame;
    private BounceFish _currentFish;

    @Override
    public void create() {
        batch = new SpriteBatch();
        image = new Texture("blob1.png");
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

        batch.begin();
        batch.draw(image, (float)_currentFish.getX(), (float)_currentFish.getY(), 200, 200);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        image.dispose();
    }
}
