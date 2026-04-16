package com.bouncefish.entities;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bouncefish.utils.GameConstants;

import java.util.function.Consumer;

public class Crab extends Creature {
    private static Animation<TextureRegion> crabAnimation;


    public Crab(float spawnTime, float spawnX, float spawnY, float speedMultiplier, Consumer<Creature> movementFunction) {
        this(spawnTime, spawnX, movementFunction);
        this.yPosition = GameConstants.WATER_LEVEL + spawnY;
        this.movementSpeedMultiplier = speedMultiplier;
    }

    // This constructor is used if we just want a default crab (Default spawn y is 0, speedMultiplier of 1)
    public Crab(float spawnTime, float spawnX, Consumer<Creature> movementFunction) {
        creatureId = 1;
        xVelocity = 700;
        yVelocity = 0;
        width = 130;
        height = 130;
        movementSpeed = 500;

        this.xPosition = spawnX;
        this.yPosition = GameConstants.WATER_LEVEL;
        this.movementSpeedMultiplier = 1;
        this.movementFunction = movementFunction;
        this.spawnTime = spawnTime;

        setBounds();
    }

    public static void leftToRight(Creature creature){
        creature.xPosition += creature.movementSpeed * creature.movementSpeedMultiplier * Gdx.graphics.getDeltaTime();
        if(creature.xPosition > GameConstants.RIGHT_DEALLOCATE_X) creature.deactivate();
    }


    public static void rightToLeft(Creature creature){
        creature.xPosition -= creature.movementSpeed * creature.movementSpeedMultiplier * Gdx.graphics.getDeltaTime();
        if(creature.xPosition < GameConstants.LEFT_DEALLOCATE_X) creature.deactivate();
    }


    @Override
    public void handleBouncedOn() {
        isDead = true;
    }
    @Override
    public TextureRegion getAnimeFrame(){
        return crabAnimation.getKeyFrame(stateTime,true);
    }
    public static void initAnime(){
        Texture texture1 = new Texture("crab1.png");
        Texture texture2 = new Texture("crab1_5.png");
        TextureRegion[] frames = new TextureRegion[2];
        frames[0] = new TextureRegion(texture1);
        frames[1] = new TextureRegion(texture2);
        crabAnimation = new Animation<>(0.5F,frames);
    }

}
