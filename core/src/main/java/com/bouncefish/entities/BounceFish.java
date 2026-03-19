package com.bouncefish.entities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.bouncefish.utils.GameConstants;

public class BounceFish extends Creature {
    private boolean isBouncing = false;

    public BounceFish() {
        xPosition = Gdx.graphics.getWidth()* 0.5;
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
}
