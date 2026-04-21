package com.bouncefish.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.bouncefish.entities.spawndata.CrabSpawnData;
import com.bouncefish.utils.GameConstants;

import java.util.function.Consumer;

public class Crab extends Creature {
    private static Animation<TextureRegion> crabAnimation;
    private static Animation<TextureRegion> crabBouncedAnimation;
    private static final float BASE_SPEED = 500;

    public Crab(float spawnTime, float spawnX, float spawnY, float speedMultiplier, Consumer<Creature> movementFunction) {
        this(spawnTime, spawnX, movementFunction);
        this.yPosition = GameConstants.WATER_LEVEL + spawnY;
        this.movementSpeedMultiplier = speedMultiplier;
        this.movementSpeed = BASE_SPEED * movementSpeedMultiplier;
        this.movementSpeed = shouldMoveLeft(spawnX) ? -movementSpeed : movementSpeed;
    }

    // This constructor is used if we just want a default crab (Default spawn y is 0, speedMultiplier of 1)
    public Crab(float spawnTime, float spawnX, Consumer<Creature> movementFunction) {
        this.creatureId = 1;
        this.xVelocity = 700;
        this.yVelocity = 0;
        this.width = 130;
        this.height = 130;
        this.movementSpeedMultiplier = 1;
        this.movementSpeed = BASE_SPEED * movementSpeedMultiplier;
        this.movementSpeed = shouldMoveLeft(spawnX) ? -movementSpeed : movementSpeed;

        this.xPosition = spawnX;
        this.yPosition = GameConstants.WATER_LEVEL;

        this.movementFunction = movementFunction;
        this.spawnTime = spawnTime;

        setBounds();
    }

    public static void normalMovement(Creature creature){
        creature.xPosition += creature.movementSpeed * Gdx.graphics.getDeltaTime();
    }

    @Override
    public void handleBouncedOn() {
        isBouncedOn = true;
        isBouncable = false;
        float newXVelocity = movingLeft? -10:10;
        setXVelocity(newXVelocity);
        setYVelocity(-500);
        setMovementFunction(Crab::bouncedOnMovement);
    }

    public static void bouncedOnMovement(Creature creature){
        creature.xPosition += creature.xVelocity * creature.movementSpeedMultiplier * Gdx.graphics.getDeltaTime();
        creature.yPosition += creature.yVelocity * creature.movementSpeedMultiplier * Gdx.graphics.getDeltaTime();
    }

    @Override
    public TextureRegion getAnimeFrame(){
        if (isBouncedOn){
            return crabBouncedAnimation.getKeyFrame(stateTime,true);
        }
        return crabAnimation.getKeyFrame(stateTime,true);
    }
    public static void initAnime(){
        Texture texture1 = new Texture("creatures/crab/crab1.png");
        Texture texture2 = new Texture("creatures/crab/crab1_5.png");
        Texture texture3 = new Texture("creatures/crab/crab2.png");
        TextureRegion[] frames = new TextureRegion[2];
        TextureRegion[] frames2 = new TextureRegion[1];
        frames[0] = new TextureRegion(texture1);
        frames[1] = new TextureRegion(texture2);
        frames2[0] = new TextureRegion(texture3);
        crabAnimation = new Animation<>(0.5F,frames);
        frames2[0] = new TextureRegion(texture3);
        crabBouncedAnimation = new Animation<>(0.5F, frames2);
    }

}
