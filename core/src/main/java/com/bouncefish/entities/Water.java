package com.bouncefish.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Water {
    private static Animation<TextureRegion> waterAnimation;
    private static float stateTime;
    public static void initAnime(){
        Texture texture1 = new Texture("water.png");
        Texture texture2 = new Texture("water_.png");
        TextureRegion[] frames = new TextureRegion[2];
        frames[0] = new TextureRegion(texture1);
        frames[1] = new TextureRegion(texture2);
        waterAnimation = new Animation<>(0.5F,frames);
    }
    public static TextureRegion getAnimeFrame(){
        return waterAnimation.getKeyFrame(stateTime,true);
    }
    public static void handleTimeStep(){
        stateTime += Gdx.graphics.getDeltaTime();
    }
}
