package com.bouncefish.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.bouncefish.entities.spawndata.CrabSpawnData;
import com.badlogic.gdx.math.Rectangle;
import com.bouncefish.utils.GameConstants;

import java.util.function.Consumer;

public class Crab extends Creature {
    private static Animation<TextureRegion> crabAnimation;
    private static final float BASE_SPEED = 500;
    private float lastStateTime;

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
        this.width = 200;
        this.height = 200;
        this.boundOffsetY = getHeight() * 0.25f;
        this.xPosition = spawnX;
        this.yPosition = GameConstants.WATER_LEVEL;

        this.xVelocity = 700;
        this.yVelocity = 0;
        this.movementSpeedMultiplier = 1;
        this.movementSpeed = BASE_SPEED * movementSpeedMultiplier;
        this.movementSpeed = shouldMoveLeft(spawnX) ? -movementSpeed : movementSpeed;
        this.movementFunction = movementFunction;
        this.spawnTime = spawnTime;

        setBounds();
    }

    @Override
    protected void setBounds(){
        this.bounds = new Rectangle(getX(), getY() + this.boundOffsetY, getWidth(), getHeight() * 0.5f);
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
        lastStateTime = stateTime;
    }

    public static void bouncedOnMovement(Creature creature){
        creature.xPosition += creature.xVelocity * creature.movementSpeedMultiplier * Gdx.graphics.getDeltaTime();
        creature.yPosition += creature.yVelocity * creature.movementSpeedMultiplier * Gdx.graphics.getDeltaTime();
    }

    @Override
    public TextureRegion getAnimeFrame(){
        /// I don't think dead animation is needed since the crab fall into the sea almost immediately
        if (isBouncedOn){
            return crabAnimation.getKeyFrame(lastStateTime,false); //lastStateTime won't increase; this just show the last frame when the crab is hit
        }

        return crabAnimation.getKeyFrame(stateTime,true);
    }
    public static void initAnime(){
        TextureRegion[] frames = new TextureRegion[9];
        for(int i=0;i<9;i++){
            frames[i] = new TextureRegion(new Texture("creatures/crab/f"+i+".png"));
        }
        crabAnimation = new Animation<>(0.2F,frames);
    }

}
