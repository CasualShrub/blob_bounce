package com.bouncefish.entities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.bouncefish.utils.GameConstants;

public class BounceFish extends Creature {
    private boolean isBouncing = false;

    public BounceFish() {
        xPosition = Gdx.graphics.getWidth()* 0.5f;
        yPosition = 800;
        xVelocity = 0;
        yVelocity = 0;

    }

    public void reverseVelocityForBounce(){
        //yVelocity = -yVelocity * GameConstants.BOUNCE_DAMPING;
        yVelocity = GameConstants.BOUNCE_VELOCITY * GameConstants.BOUNCE_DAMPING;
        isBouncing = true;

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isBouncing = false;
            }
        }, 0.5f);
    }

    public void applyFriction() {
        xVelocity *= GameConstants.FRICTION;
    }

    public boolean isBouncing() {
        return isBouncing;
    }

    @Override
    public void handleTimeStep() {
        // Force of gravity
        decrementYVelocity(GameConstants.GRAVITY);

        // Update x and y position based on velocity
        updatePosition();

        // Handle bouncing off the ground (TODO: refactor to collision check and bouncing off a creature)
        if (getY() <= GameConstants.GROUND_HEIGHT) {
            setY(GameConstants.GROUND_HEIGHT);
            reverseVelocityForBounce();
            applyFriction();

            // Stop tiny bounces
            if (Math.abs(getYVelocity()) < 1.0) {
                setYVelocity(0);
            }
        }

        //TODO: Audit? Might not be necessary if we are just using flat velocity instead of acceleration

        // Bouncing off left wall
        if (getX() <= 0) {
            setX(0);
            setXVelocity(Math.abs(getXVelocity()) * GameConstants.BOUNCE_DAMPING);
        }

        // Bouncing off right wall
        if (getX() >= Gdx.graphics.getWidth() - 250) {
            setX(Gdx.graphics.getWidth() - 250);
            setXVelocity(-Math.abs(getXVelocity()) * GameConstants.BOUNCE_DAMPING);
        }
    }
}
