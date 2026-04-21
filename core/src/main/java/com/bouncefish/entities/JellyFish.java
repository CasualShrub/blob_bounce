package com.bouncefish.entities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.bouncefish.utils.GameConstants;

import java.util.function.Consumer;

public class JellyFish extends Creature {
    //y = scale * sin( angularFreq * x), just a reminder
    private static float angularFrequency = 0.01F;
    private static float scale = 500F;
    private static Animation<TextureRegion> jellyFishAnimation;

    public JellyFish(float spawnTime, float spawnX, float spawnY, float speedMultiplier, Consumer<Creature> movementFunction){
        creatureId = 2;
        width = 120;
        height = 120;

        this.xVelocity = 300;
        this.yVelocity = 0;
        this.xPosition = spawnX;
        this.yPosition = spawnY;
        this.movementSpeedMultiplier = speedMultiplier;
        this.movementFunction = movementFunction;
        this.spawnTime = spawnTime;

        setBounds();
    }

    public static void jellyFishLeftToRightMovement(Creature creature){
        creature.xPosition += creature.xVelocity * creature.movementSpeedMultiplier * Gdx.graphics.getDeltaTime();
        creature.setYVelocity(scale * MathUtils.cos(angularFrequency * creature.xPosition));
        creature.yPosition += creature.yVelocity * Gdx.graphics.getDeltaTime();
        if(creature.xPosition > GameConstants.RIGHT_DEALLOCATE_X) creature.deactivate();
    }

    public static void jellyFishRightToLeftMovement(Creature creature){
        creature.xPosition -= creature.xVelocity * creature.movementSpeedMultiplier * Gdx.graphics.getDeltaTime();
        creature.setYVelocity(scale * MathUtils.cos(angularFrequency * creature.xPosition));
        creature.yPosition += creature.yVelocity * Gdx.graphics.getDeltaTime();
        if(creature.xPosition < GameConstants.LEFT_DEALLOCATE_X) creature.deactivate();
    }

    @Override
    public void handleBouncedOn() {

    }

    public void setAngularFrequency(float newFre){
        angularFrequency = newFre;
    }

    public void setScale(int newScale){
        scale = newScale;
    }

    @Override
    public TextureRegion getAnimeFrame(){
        return jellyFishAnimation.getKeyFrame(stateTime,true);

    }
    public static void initAnime(){
        TextureRegion[] frames = new TextureRegion[15];
        for(int i=0;i<15;i++){
            frames[i] = new TextureRegion(new Texture("creatures/jellyfish/f"+i+".png"));
        }

        jellyFishAnimation = new Animation<>(0.08f, frames);
    }

}
