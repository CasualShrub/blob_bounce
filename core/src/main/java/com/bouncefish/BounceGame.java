package com.bouncefish;

import com.badlogic.gdx.Gdx;
import com.bouncefish.entities.BounceFish;
import com.bouncefish.utils.GameConstants;

import javax.swing.Timer;

public class BounceGame {

    private BounceFish _bounceFish;
    Timer _timer;
    private boolean _leftPressed = false;
    private boolean _rightPressed = false;

    public BounceGame() {
        _bounceFish = new BounceFish();
    }

    public void timeStep() {
        // Force of gravity
        _bounceFish.decrementYVelocity(GameConstants.GRAVITY);

        // Update x and y position based on velocity
        _bounceFish.updatePosition();

        // Handle bouncing off the ground (TODO: refactor to collision check and bouncing off a creature)
        if (_bounceFish.getY() <= GameConstants.GROUND_HEIGHT) {
            _bounceFish.setY(GameConstants.GROUND_HEIGHT);
            System.out.println("Velocity Before Bounce: " + _bounceFish.getYVelocity());
            _bounceFish.reverseVelocityForBounce();
            System.out.println("Velocity After Bounce: " + _bounceFish.getYVelocity());
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
