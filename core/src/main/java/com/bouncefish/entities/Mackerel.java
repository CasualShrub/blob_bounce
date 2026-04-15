package com.bouncefish.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.function.Consumer;

public class Mackerel extends OrdinaryFish{
    private static Animation<TextureRegion> mackerelAnimation;
    public Mackerel(float spawnTime, float startX, float endX, Consumer<Creature> movementFunction) {
        super(startX,endX);
        creatureId = 4;
        xVelocity = movingLeft ? -700 : 700;
        yVelocity = 0;
        width = 130;
        height = 130;
        movementSpeed = 0;

        this.movementSpeedMultiplier = 1;
        this.movementFunction = movementFunction;
        this.spawnTime = spawnTime;

        setBounds();
    }
    public static void initAnime(){
        Texture texture1 = new Texture("アジ.png");
        Texture texture2 = new Texture("アジ2.png");
        TextureRegion[] frames = new TextureRegion[2];
        frames[0] = new TextureRegion(texture1);
        frames[1] = new TextureRegion(texture2);
        mackerelAnimation = new Animation<>(0.5F,frames);
    }
    @Override
    public TextureRegion getAnimeFrame() {
        return mackerelAnimation.getKeyFrame(stateTime,true);
    }
}
