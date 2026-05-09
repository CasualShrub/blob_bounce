package com.bouncefish.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.bouncefish.utils.GameConstants;

public class Water {
    private static Texture waterTexture;
    private static TextureRegion waterRegion;
    private static float stateTime;
    private static Animation<TextureRegion> splashAnimation;
    private static Array<Float> splashX = new Array<>();
    private static Array<Float> splashTime = new Array<>();
    private static float splashWidth = 256f;
    private static float splashHeight = 256f;


    public static void initAnime() {
        waterTexture = new Texture("water.png");
        waterTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);

        waterRegion = new TextureRegion(waterTexture);

        TextureRegion[] frames = new TextureRegion[6];
        for(int i=0;i<6;i++){
            frames[i] = new TextureRegion(new Texture("splash/splash"+i+".png"));
        }

        splashAnimation = new Animation<>(0.1f, frames);
        splashAnimation.setPlayMode(Animation.PlayMode.NORMAL);
    }

    public static TextureRegion getAnimeFrame() {
        float scrollSpeed = 0.1f; // higher = faster

        float u = (stateTime * scrollSpeed) % 1f;

        waterRegion.setRegion(u, 0f, u + 1f, 1f);

        return waterRegion;
    }
    public static void setSplashSize(float width, float height) { //in case we want different sizes
        splashWidth = width;
        splashHeight = height;
    }
    public static void playSplash(float x) {
        splashX.add(x);
        splashTime.add(0f);
    }
    public static Array<Sprite> getSplashFrames() {
        float delta = Gdx.graphics.getDeltaTime();
        Array<Sprite> frames = new Array<>();

        for (int i = splashTime.size - 1; i >= 0; i--) {
            float time = splashTime.get(i) + delta;
            splashTime.set(i, time);

            if (splashAnimation.isAnimationFinished(time)) {
                splashTime.removeIndex(i);
                splashX.removeIndex(i);
                continue;
            }

            Sprite sprite = new Sprite(splashAnimation.getKeyFrame(time, false));
            sprite.setSize(splashWidth, splashHeight);
            sprite.setPosition(splashX.get(i), GameConstants.WATER_LEVEL);

            frames.add(sprite);
        }

        return frames;
    }

    public static float getSplashX(int index) {
        return splashX.get(index);
    }

    public static void handleTimeStep() {
        stateTime += Gdx.graphics.getDeltaTime();
    }
}
