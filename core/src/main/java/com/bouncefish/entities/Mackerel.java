package com.bouncefish.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bouncefish.utils.GameConstants;

import java.util.function.Consumer;

public class Mackerel extends OrdinaryFish{
    private static Animation<TextureRegion> mackerelAnimation;
    private final int BASE_SPEED = 700;
    public Mackerel(float spawnTime, float startX, float endX, float speedMultiplier, Consumer<Creature> movementFunction) {
        super(startX,endX);
        creatureId = 4;
        width = 130;
        height = 130;

        this.movementSpeedMultiplier = speedMultiplier;
        xVelocity = movingLeft ? -BASE_SPEED : BASE_SPEED;
        this.xVelocity *= movementSpeedMultiplier;

        this.movementFunction = movementFunction;
        this.spawnTime = spawnTime;

        setBounds();
    }
    public static void initAnime(){
        TextureRegion[] frames = new TextureRegion[6];
        for(int i=0;i<6;i++){
            frames[i] = new TextureRegion(new Texture("creatures/fish/f"+i+".png"));
        }
        mackerelAnimation = new Animation<>(0.15F,frames); // my God, it looks so ugly

        //TODO: add dead animation animation
    }
    @Override
    public TextureRegion getAnimeFrame() {
        return mackerelAnimation.getKeyFrame(stateTime,true);
    }
}
