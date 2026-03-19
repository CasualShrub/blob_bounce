package com.bouncefish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.bouncefish.entities.BounceFish;
import com.bouncefish.utils.GameConstants;

import javax.swing.Timer;

public class BounceGame implements GestureDetector.GestureListener {

    private BounceFish _bounceFish;
    Timer _timer;
    private boolean _leftPressed;
    private boolean _rightPressed;

    public BounceGame() {
        _bounceFish = new BounceFish();
        Gdx.input.setInputProcessor(new GestureDetector(this));
    }

    public void timeStep() {
        if (_leftPressed) {
            _bounceFish.setXVelocity(-GameConstants.H_SPEED);
        }
        else if (_rightPressed) {
            _bounceFish.setXVelocity(GameConstants.H_SPEED);
        }

        // Force of gravity
        _bounceFish.decrementYVelocity(GameConstants.GRAVITY);

        // Update x and y position based on velocity
        _bounceFish.updatePosition();

        // Handle bouncing off the ground (TODO: refactor to collision check and bouncing off a creature)
        if (_bounceFish.getY() <= GameConstants.GROUND_HEIGHT) {
            _bounceFish.setY(GameConstants.GROUND_HEIGHT);
            _bounceFish.reverseVelocityForBounce();
            _bounceFish.applyFriction();

            // Stop tiny bounces
            if (Math.abs(_bounceFish.getYVelocity()) < 1.0) {
                _bounceFish.setYVelocity(0);
            }
        }

        //TODO: Audit? Might not be necessary if we are just using flat velocity instead of acceleration

        // Bouncing off left wall
        if (_bounceFish.getX() <= 0) {
            _bounceFish.setX(0);
            _bounceFish.setXVelocity(Math.abs(_bounceFish.getXVelocity()) * GameConstants.BOUNCE_DAMPING);
        }

        // Bouncing off right wall
        if (_bounceFish.getX() >= Gdx.graphics.getWidth() - 250) {
            _bounceFish.setX(Gdx.graphics.getWidth() - 250);
            _bounceFish.setXVelocity(-Math.abs(_bounceFish.getXVelocity()) * GameConstants.BOUNCE_DAMPING);
        }
    }

    public BounceFish getBounceFish() {
        return _bounceFish;
    }


    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        if (x > Gdx.graphics.getWidth() - 200) {
            _leftPressed = false;
            _rightPressed = true;
            return true;
        }
        else if (x < 200){
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
