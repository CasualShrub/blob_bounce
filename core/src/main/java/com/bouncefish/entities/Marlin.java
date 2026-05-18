package com.bouncefish.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.bouncefish.gameplay.ProgressTracker;
import com.bouncefish.utils.GameConstants;
import java.util.function.Consumer;
import com.badlogic.gdx.Gdx;

public class Marlin extends Creature {
    private static Animation<TextureRegion> marlinAnimation;
    private static Animation<TextureRegion> marlinBouncedAnimation;
    private static final float BASE_SPEED = 1500; // Marlin is a fast fish

//    public Marlin(float spawnTime, float spawnX, float spawnY, float speedMultiplier, Consumer<Creature> movementFunction) {
//        this(spawnTime, spawnX,spawnY, movementFunction);
//        this.yPosition = GameConstants.WATER_LEVEL + spawnY;
//        this.movementSpeedMultiplier = speedMultiplier;
//        this.movementSpeed = BASE_SPEED * movementSpeedMultiplier;
//        this.movementSpeed = shouldMoveLeft(spawnX) ? -this.movementSpeed : this.movementSpeed;
//        this.movingLeft = shouldMoveLeft(spawnX);
//    }

    public Marlin(float spawnTime, float spawnX, float spawnY,float speedMultiplier, Consumer<Creature> movementFunction) {
        this.creatureId = 6;
        this.width = 450;
        this.height = 225;
        this.xPosition = spawnX;
        this.yPosition = spawnY;
        //this.yPosition = GameConstants.WATER_LEVEL + 400; // Spawns above water

        this.movementSpeedMultiplier = speedMultiplier;
        this.movementSpeed = BASE_SPEED* speedMultiplier;
        this.movementSpeed = shouldMoveLeft(spawnX) ? -this.movementSpeed : this.movementSpeed;
        this.movingLeft = shouldMoveLeft(spawnX);

        this.movementFunction = movementFunction;
        this.spawnTime = spawnTime;

        setBounds();
    }

//    @Override
//    protected void setBounds(){
//        this.bounds = new Rectangle(getX(), getY() + this.boundOffsetY, getWidth(), getHeight() * 0.5f);
//    }

    public static void normalMovement(Creature creature){
        creature.xPosition += creature.movementSpeed * Gdx.graphics.getDeltaTime();
    }

    @Override
    public void handleBouncedOn() {
        isBouncedOn = true;
        isBouncable = false;
        // Bounce off to the side and down fast
        float newXVelocity = movingLeft ? -300 : 300;
        setXVelocity(newXVelocity);
        setYVelocity(-1200);

        setMovementFunction(Marlin::bouncedOnMovement);

        // Awards 100 points but only 1 creature hit (logic handled in ProgressTracker)
        ProgressTracker.increaseScore(100);
    }

    public static void bouncedOnMovement(Creature creature){
        creature.xPosition += creature.xVelocity * Gdx.graphics.getDeltaTime();
        creature.yPosition += creature.yVelocity * Gdx.graphics.getDeltaTime();

        // Trigger water splash when falling back into the sea
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
        TextureRegion[] frames = new TextureRegion[7];
        // Loading the 7 frames: IMG_2286.PNG to IMG_2292.PNG
        for(int i = 0; i < 7; i++){
            int imgNum = 2286 + i;
            frames[i] = new TextureRegion(new Texture("creatures/marlin/IMG_" + imgNum + ".PNG"));
        }
        // Fast animation for a fast fish (0.08s per frame)
        marlinAnimation = new Animation<>(0.08F, frames);
        marlinBouncedAnimation = new Animation<>(0.08F, frames);
    }
}
