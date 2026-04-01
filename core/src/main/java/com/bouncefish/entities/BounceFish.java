package com.bouncefish.entities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;
import com.bouncefish.gameplay.SoundManager;
import com.bouncefish.utils.GameConstants;

import java.util.ArrayList;

public class BounceFish extends Creature {
    private boolean isBouncing = false;
    private float paralyzedTime;
    private boolean isParalyzed = false;
    private ArrayList<Creature> creatureList; //Fish now knows what other objects exist in the game

    public BounceFish() {
        creatureId = 0;
        xPosition = Gdx.graphics.getWidth()* 0.5f;
        yPosition = 800;
        xVelocity = 0;
        yVelocity = 0;
        width = 130;
        height = 130;
        setBounds(); //Fish has a physical size needed for collision
    }

    public void reverseVelocityForBounce(){
        double currentY = getY();
        reverseVelocityForBounce(currentY);
    }

    public void reverseVelocityForBounce(double currentY){
        double targetY = Gdx.graphics.getHeight();
        reverseVelocityForBounce(targetY, currentY);
    }

    public void reverseVelocityForBounce(double targetY, double currentY){
        double deltaY = targetY - height - currentY;
        yVelocity = Math.sqrt(2 * GameConstants.GRAVITY * deltaY);
        isBouncing = true;

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isBouncing = false;
            }
        }, 0.5f);
    }

    public void applyDeathVelocity(){
        reverseVelocityForBounce(getY() + 300, getY());
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
        if(isParalyzed){
            paralyzedTime += Gdx.graphics.getDeltaTime();
            if(paralyzedTime > GameConstants.MAX_PARALYZED_TIME){
                isParalyzed = false;
                paralyzedTime = 0F;
            }
        }
        //If fish touches any creature, Fish gets placed on top of creature and bounces upward.
        //Like Mario jumping on Enemy
        for (Creature creature : creatureList) {
            if(this.bounds.overlaps(creature.bounds) && !isParalyzed && creature.isBouncable()){

                if(creature.getCreatureId() == 2){//touches jellyfish
                    isParalyzed = true;
                    break;
                }

                //should check yVelocity until here since bouncefish could hit jellyfish from below
                if(yVelocity < 0) {
                    setY(creature.getHeight() + creature.getY());
                    reverseVelocityForBounce();

                    //TODO: call the getBouncedOn method of creature
                    SoundManager.playBounceSound();
                    break;
                }
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
                SoundManager.playBounceSound();
            }
        }


        //TODO: Audit? Might not be necessary if we are just using flat velocity instead of acceleration

        // Bouncing off left wall
        if (getX() <= 0) { //Fish reached left Edge
            setX(0); // Don't let it go outside screen
            setXVelocity(Math.abs(getXVelocity()) * GameConstants.BOUNCE_DAMPING); // -5 -> +5 After Bounce Should go Right(Positive)
        }

        // Bouncing off right wall
        if (getX() >= Gdx.graphics.getWidth() - 250) {
            setX(Gdx.graphics.getWidth() - 250);
            setXVelocity(-Math.abs(getXVelocity()) * GameConstants.BOUNCE_DAMPING);
        }
    }

    @Override
    public void handleBouncedOn() {
        // Do nothing
        System.out.println("This should never happen");
    }

    public void updateCreatureList(ArrayList<Creature> creatureList){
        this.creatureList = creatureList;
    }

    public boolean isParalyzed(){
        return isParalyzed;
    }
}
