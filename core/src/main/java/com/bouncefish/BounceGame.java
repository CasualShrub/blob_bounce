package com.bouncefish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.bouncefish.entities.BounceFish;
import com.bouncefish.entities.*;
import com.bouncefish.utils.GameConstants;

import javax.swing.Timer;
import java.util.ArrayList;

public class BounceGame implements GestureDetector.GestureListener {

    private BounceFish _bounceFish;
    Timer _timer;
    private boolean _leftPressed;
    private boolean _rightPressed;
    private ArrayList<Creature> _creatureList;

    public BounceGame() {
        Gdx.input.setInputProcessor(new GestureDetector(this));

        _bounceFish = new BounceFish();
        _creatureList = new ArrayList<Creature>();
        //make this a wave system???
        int startX = 0;
        for (int i = 0; i < 100; i++) {
            Creature crab = new Crab();
            crab.setX(startX - (300 * i));
            _creatureList.add(crab);
        }
    }

    // What do we do every frame?
    public void timeStep() {
        if (!Gdx.input.isTouched()){
            _leftPressed = false;
            _rightPressed = false;
            _bounceFish.setXVelocity(0);
        }

        if (_leftPressed) {
            _bounceFish.setXVelocity(-GameConstants.H_SPEED);
        }
        else if (_rightPressed) {
            _bounceFish.setXVelocity(GameConstants.H_SPEED);
        }

        _bounceFish.handleTimeStep();
        for (Creature creature: _creatureList) {
            creature.handleTimeStep();
        }
    }

    public BounceFish getBounceFish() {
        return _bounceFish;
    }

    public ArrayList<Creature> getCreatureList() {
        return _creatureList;
    }


    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        if (x > Gdx.graphics.getWidth() - GameConstants.RIGHT_CONTROL_BORDER) {
            _leftPressed = false;
            _rightPressed = true;
            return true;
        }
        else if (x < GameConstants.LEFT_CONTROL_BORDER){
            _leftPressed = true;
            _rightPressed = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
