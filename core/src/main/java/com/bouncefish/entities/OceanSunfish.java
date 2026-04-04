package com.bouncefish.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.bouncefish.utils.GameConstants;

import java.util.function.Consumer;

public class OceanSunfish extends Creature { //Haven't test it yet
    private int flipCounter = 100;//counter used to decide when to flip. Use delta time to make it consistent
    private static Animation<TextureRegion> oceanSunfishAnimation;
    public OceanSunfish(float spawnTime, float spawnX, float spawnY, float speedMultiplier, Consumer<Creature> movementFunction) {
        creatureId = 3;
        xVelocity = 0;
        yVelocity = 0;
        width = 130;
        height = 130;
        movementSpeed = 12;

        this.xPosition = spawnX;
        this.yPosition = spawnY;
        this.movementSpeedMultiplier = speedMultiplier;
        this.movementFunction = movementFunction;
        this.spawnTime = spawnTime;

        setBounds();
    }

    public static void crabMovementLeftToRight(Creature creature){
        creature.xPosition += creature.movementSpeed;
    }

    public static void crabMovementRightToLeft(Creature creature){
        creature.xPosition -= creature.movementSpeed;
    }

    @Override
    public void handleBouncedOn() {
        isDead = true;
    }
    @Override
    public void handleTimeStep(){
        stateTime += Gdx.graphics.getDeltaTime();
        applyMovement();
        updateBounds();

        if(--flipCounter < 0){
            flip();
        }
    }
    private void flip(){ //flips itself. So

        if(isBouncable){
            //TODO play flipping animation
            isBouncable = false;
        }else{
            //TODO play flipping animation
            isBouncable = true;
        }
        flipCounter = 100;

    }
    @Override
    public TextureRegion getAnimeFrame(){
        return oceanSunfishAnimation.getKeyFrame(stateTime,true);
    }

}
