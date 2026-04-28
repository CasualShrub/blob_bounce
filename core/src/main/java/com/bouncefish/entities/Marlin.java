package com.bouncefish.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bouncefish.gameplay.ProgressTracker;
import com.bouncefish.utils.GameConstants;
import java.util.function.Consumer;
import com.badlogic.gdx.Gdx;
import com.bouncefish.entities.Creature;


public class Marlin extends Creature {
    private static Animation<TextureRegion> marlinAnimation;
    private static Animation<TextureRegion> marlinBouncedAnimation;
    private static final float BASE_SPEED = 500; //make it slower for now for testing; 1500 is better

    public Marlin(float spawnTime, float spawnX, float spawnY, float speedMultiplier, Consumer<Creature> movementFunction) {
        this(spawnTime, spawnX, movementFunction);
        this.yPosition = GameConstants.WATER_LEVEL + spawnY;
        this.movementSpeedMultiplier = speedMultiplier;
        this.movementSpeed = BASE_SPEED * movementSpeedMultiplier;
        this.movementSpeed = shouldMoveLeft(spawnX) ? -movementSpeed : movementSpeed;
    }

    // This constructor is used if we just want a default crab (Default spawn y is 0, speedMultiplier of 1)
    public Marlin(float spawnTime, float spawnX, Consumer<Creature> movementFunction) {
        this.creatureId = 6;
        this.xVelocity = 1400;
        this.yVelocity = 0;
        this.width = 260;
        this.height = 260;
        this.movementSpeedMultiplier = 1;
        //this.movementSpeed = BASE_SPEED * movementSpeedMultiplier;
        //this.movementSpeed = shouldMoveLeft(spawnX) ? -movementSpeed : movementSpeed;

        this.xPosition = spawnX;
        //this.yPosition = GameConstants.WATER_LEVEL;

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
        setYVelocity(-800);
        setMovementFunction(com.bouncefish.entities.Crab::bouncedOnMovement);

        ProgressTracker.increaseScore(100);
    }

    public static void bouncedOnMovement(Creature creature){
        creature.xPosition += creature.xVelocity * creature.movementSpeedMultiplier * Gdx.graphics.getDeltaTime();
        creature.yPosition += creature.yVelocity * creature.movementSpeedMultiplier * Gdx.graphics.getDeltaTime();
    }

    @Override
    public TextureRegion getAnimeFrame(){
        if (isBouncedOn){
            return marlinBouncedAnimation.getKeyFrame(stateTime,true);
        }
        return marlinAnimation.getKeyFrame(stateTime,true);
    }
    public static void initAnime(){
        TextureRegion[] frames = new TextureRegion[1];
        frames[0] = new TextureRegion(new Texture("creatures/marlin/marlin.png"));
        marlinAnimation = new Animation<>(0.2F,frames);

        TextureRegion[] frames_ = new TextureRegion[1];
        frames[0] = new TextureRegion(new Texture("creatures/marlin/marlin.png"));
        marlinBouncedAnimation = new Animation<>(0.2F,frames);
    }

}
