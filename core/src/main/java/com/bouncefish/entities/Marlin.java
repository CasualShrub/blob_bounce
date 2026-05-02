package com.bouncefish.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bouncefish.gameplay.ProgressTracker;
import com.bouncefish.utils.GameConstants;
import java.util.function.Consumer;
import com.badlogic.gdx.Gdx;

public class Marlin extends Creature {
    private static Animation<TextureRegion> marlinAnimation;
    private static Animation<TextureRegion> marlinBouncedAnimation;

    public Marlin(float spawnTime, float spawnX, float spawnY, float speedMultiplier, Consumer<Creature> movementFunction) {
        this(spawnTime, spawnX, movementFunction);
        this.yPosition = GameConstants.WATER_LEVEL + spawnY;
        this.movementSpeedMultiplier = speedMultiplier;
        this.movementSpeed = 500 * movementSpeedMultiplier;
        this.movementSpeed = shouldMoveLeft(spawnX) ? -movementSpeed : movementSpeed;
    }

    public Marlin(float spawnTime, float spawnX, Consumer<Creature> movementFunction) {
        this.creatureId = 6;
        this.xVelocity = 1400;
        this.yVelocity = 0;
        this.width = 260;
        this.height = 260;
        this.movementSpeedMultiplier = 1;
        this.xPosition = spawnX;

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
        float newXVelocity = movingLeft ? -10 : 10;
        setXVelocity(newXVelocity);
        setYVelocity(-800);

        // Use Marlin's specific bounced movement for falling into water logic
        setMovementFunction(Marlin::bouncedOnMovement);

        // Adds 100 to total score, but only counts as 1 creature hit
        ProgressTracker.increaseScore(100);
    }

    public static void bouncedOnMovement(Creature creature){
        creature.xPosition += creature.xVelocity * creature.movementSpeedMultiplier * Gdx.graphics.getDeltaTime();
        creature.yPosition += creature.yVelocity * creature.movementSpeedMultiplier * Gdx.graphics.getDeltaTime();

        // Handle water splash when falling back
        if(!creature.inWater && creature.yPosition <= GameConstants.WATER_LEVEL){
            creature.inWater = true;
            Water.playSplash(creature.xPosition);
        }
    }

    @Override
    public TextureRegion getAnimeFrame(){
        if (isBouncedOn){
            return marlinBouncedAnimation.getKeyFrame(stateTime, true);
        }
        return marlinAnimation.getKeyFrame(stateTime, true);
    }

    public static void initAnime(){
        TextureRegion[] frames = new TextureRegion[1];
        frames[0] = new TextureRegion(new Texture("creatures/marlin/marlin.png"));
        marlinAnimation = new Animation<>(0.2F, frames);

        TextureRegion[] bouncedFrames = new TextureRegion[1];
        bouncedFrames[0] = new TextureRegion(new Texture("creatures/marlin/marlin.png"));
        marlinBouncedAnimation = new Animation<>(0.2F, bouncedFrames);
    }
}
