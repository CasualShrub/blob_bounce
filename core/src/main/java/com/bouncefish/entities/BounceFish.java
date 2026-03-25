package com.bouncefish.entities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.bouncefish.BounceGame;
import com.bouncefish.utils.GameConstants;

import java.util.ArrayList;

public class BounceFish extends Creature {
    private boolean isBouncing = false;
    private ArrayList<Creature> creatureList;

    public BounceFish() {
        xPosition = Gdx.graphics.getWidth()* 0.5f;
        yPosition = 800;
        xVelocity = 0;
        yVelocity = 0;
        width = 120;
        height = 120;
        setBounds();
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
        updateBounds();

        for (Creature creature : creatureList) {
            if(this.bounds.overlaps(creature.bounds)){
                setY(creature.getHeight());
                reverseVelocityForBounce();

                //TODO: call the getBouncedOn method of creature
                break;
            }
        }

        //Lose Game! Initiate game over
        if (getY() <= GameConstants.GROUND_HEIGHT) {
            System.out.println("You died!"); //TODO: wire this to the game manager
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

    public void updateCreatureList(ArrayList<Creature> creatureList){
        this.creatureList = creatureList;
    }
}
