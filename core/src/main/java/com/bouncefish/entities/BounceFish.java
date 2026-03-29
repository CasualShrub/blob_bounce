package com.bouncefish.entities;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.bouncefish.BounceGame;
import com.bouncefish.utils.GameConstants;

import java.util.ArrayList;

public class BounceFish extends Creature {
    private boolean isBouncing = false;
    private boolean isDead = false;
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

    public void reverseVelocityForBounce(double multiplier){
        //yVelocity = -yVelocity * GameConstants.BOUNCE_DAMPING;
        yVelocity = GameConstants.BOUNCE_VELOCITY * GameConstants.BOUNCE_DAMPING - (GameConstants.BOUNCE_VELOCITY * multiplier);
        isBouncing = true;

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isBouncing = false;
            }
        }, 0.5f);
    }

    public void applyDeathVelocity(){
        yVelocity = GameConstants.BOUNCE_VELOCITY * GameConstants.DEATH_BOUNCE_DAMPING;
    }

    public void applyFriction() {
        xVelocity *= GameConstants.FRICTION;
    }

    public boolean isBouncing() {
        return isBouncing;
    }
    public boolean isDead() {
        return isDead;
    }

    @Override
    public void handleTimeStep() {
        // Force of gravity
        decrementYVelocity(GameConstants.GRAVITY);

        // Update x and y position based on velocity
        updatePosition();
        updateBounds();

        if (isDead){
            return;
        }

        for (Creature creature : creatureList) {
            if(this.bounds.overlaps(creature.bounds)){
                setY(creature.getHeight());
                reverseVelocityForBounce(((double) creature.height + creature.getX()) / Gdx.graphics.getHeight());

                //TODO: call the getBouncedOn method of creature
                break;
            }
        }

        // Lose Game! Initiate game over
        if (!GameConstants.IS_IMMORTAL){
            if (getY() <= GameConstants.GROUND_HEIGHT) {
                isDead = true;
                setY(GameConstants.GROUND_HEIGHT);
                applyDeathVelocity();
                xVelocity *= -1.2;
            }
        }
        // Keep bouncing if you are immortal.
        else {
            if (getY() <= GameConstants.GROUND_HEIGHT) {
                setY(GameConstants.GROUND_HEIGHT);
                reverseVelocityForBounce(0);
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
