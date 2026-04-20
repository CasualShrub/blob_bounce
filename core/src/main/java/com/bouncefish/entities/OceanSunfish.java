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
    private static final float BASE_SPEED = 200;

    public OceanSunfish(float spawnTime, float spawnX, float spawnY, float speedMultiplier, Consumer<Creature> movementFunction) {
        this.creatureId = 3;
        this.width = 260;
        this.height = 260;

        this.xPosition = spawnX;
        this.yPosition = spawnY;

        this.movementSpeedMultiplier = speedMultiplier;
        this.movementSpeed = BASE_SPEED * movementSpeedMultiplier;
        this.movementSpeed = shouldMoveLeft(spawnX) ? -movementSpeed : movementSpeed;
        this.yVelocity = 0;

        this.movementFunction = movementFunction;
        this.spawnTime = spawnTime;

        setBoundScale(1F,0.42F);
        this.boundScaleY = 0.42F;
        setBounds();
    }

    public static void normalMovement(Creature creature){
        creature.xPosition += creature.movementSpeed * Gdx.graphics.getDeltaTime();
    }

    @Override
    public void handleBouncedOn() {
        isBouncedOn = true;
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
        Texture texture1 = new Texture("creatures/sunfish/o2.png");
        Texture texture2 = new Texture("creatures/sunfish/o1.png");
        TextureRegion[] frames1 = new TextureRegion[1];
        TextureRegion[] frames2 = new TextureRegion[1];
        frames1[0] = new TextureRegion(texture1);
        frames2[0] = new TextureRegion(texture2);
        oceanSunfishAnimationNormal = new Animation<>(0.5f, frames1);
        oceanSunfishAnimationFlipped = new Animation<>(0.5f, frames2);
    }

}
