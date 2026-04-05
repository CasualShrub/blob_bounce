package com.bouncefish.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.bouncefish.utils.GameConstants;

import java.util.function.Consumer;

public class OceanSunfish extends Creature {
    private int flipCounter = 100;//counter used to decide when to flip. Use delta time to make it consistent
    private static Animation<TextureRegion> oceanSunfishAnimationNormal;
    private static Animation<TextureRegion> oceanSunfishAnimationFlipped;

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
    public OceanSunfish(float spawnTime, float spawnX, Consumer<Creature> movementFunction) {
        creatureId = 3;
        xVelocity = 0;
        yVelocity = 0;
        width = 260;
        height = 260;
        movementSpeed = 5;

        this.xPosition = spawnX;
        this.yPosition = GameConstants.Screen_Height/2;
        this.movementSpeedMultiplier = 1;
        this.movementFunction = movementFunction;
        this.spawnTime = spawnTime;

        setBounds();
    }
    @Override
    public void setBounds(){
        bounds = new Rectangle((int)getX(), (int)getY(),
            GameConstants.OCEANSUNFISH_WIDTH, GameConstants.BOUNCEFISH_HEIGHT);
    }
    public static void OceanSunfishMovementLeftToRight(Creature creature){
        creature.xPosition += creature.movementSpeed;
    }

    public static void OceanSunfishMovementRightToLeft(Creature creature){
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
        if(isBouncable){
            return oceanSunfishAnimationNormal.getKeyFrame(stateTime,true);
        }else{
            return oceanSunfishAnimationFlipped.getKeyFrame(stateTime,true);
        }

    }
    public static void initAnime(){
        Texture texture1 = new Texture("o2.png");
        Texture texture2 = new Texture("o1.png");
        TextureRegion[] frames1 = new TextureRegion[1];
        TextureRegion[] frames2 = new TextureRegion[1];
        frames1[0] = new TextureRegion(texture1);
        frames2[0] = new TextureRegion(texture2);
        oceanSunfishAnimationNormal = new Animation<>(0.5f, frames1);
        oceanSunfishAnimationFlipped = new Animation<>(0.5f, frames2);
    }

}
