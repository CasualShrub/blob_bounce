package com.bouncefish;

import com.badlogic.gdx.Gdx;
import com.bouncefish.entities.BounceFish;
import com.bouncefish.utils.GameConstants;

import javax.swing.Timer;

public class BounceGame {

    private BounceFish _bounceFish;
    Timer _timer;
    private boolean _leftPressed;
    private boolean _rightPressed;

    public BounceGame() {
        _bounceFish = new BounceFish();
    }

    public void timeStep() {
        if (Gdx.input.isTouched()){ // Let's start simple just to debug: move right if screen is touched is pressed
            System.out.println("bruh");
            _leftPressed = false;
            _rightPressed = true;
        }
        else {
            _leftPressed = true;
            _rightPressed = false;
        }

        if (_leftPressed) {
            _bounceFish.decrementXVelocity(GameConstants.H_ACCEL);
            if (_bounceFish.getXVelocity() < -GameConstants.MAX_H_SPEED) {
                _bounceFish.setXVelocity(-GameConstants.MAX_H_SPEED);
            }
        }
        else if (_rightPressed) {
            _bounceFish.incrementXVelocity(GameConstants.H_ACCEL);
            if (_bounceFish.getXVelocity() > GameConstants.MAX_H_SPEED) {
                _bounceFish.setXVelocity(GameConstants.MAX_H_SPEED);
            }
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
    }

    public BounceFish getBounceFish() {

        return _bounceFish;
    }


}
