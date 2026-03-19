package com.bouncefish.entities;
import com.badlogic.gdx.Gdx;
import com.bouncefish.utils.GameConstants;

public class BounceFish extends Creature {

    public BounceFish() {
        xPosition = Gdx.graphics.getWidth()* 0.5;
        yPosition = 800;
        xVelocity = 0;
        yVelocity = 0;
    }

    public void reverseVelocityForBounce(){
        yVelocity = -yVelocity * GameConstants.BOUNCE_DAMPING;
    }

    public void applyFriction() {
        xVelocity *= GameConstants.FRICTION;
    }

}
