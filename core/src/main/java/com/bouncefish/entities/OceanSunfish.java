package com.bouncefish.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.bouncefish.utils.GameConstants;

import java.util.function.Consumer;

public class OceanSunfish extends Creature {
    protected static int defaultFlipCounter = 250;
    private int flipCounter = defaultFlipCounter;//counter used to decide when to flip. Use delta time to make it consistent
    private static Animation<TextureRegion> oceanSunfishAnimationNormal;
    private static Animation<TextureRegion> oceanSunfishAnimationFlipped;
    private static final float BASE_SPEED = 100;
    private boolean isShaking = false;
    private float shakeTimer = 0f;

    private static final float SHAKE_DURATION = 0.5f;
    private static final float SHAKE_INTENSITY = 6f;
    private float originalX;

    public OceanSunfish(float spawnTime, float spawnX, float spawnY, float speedMultiplier, Consumer<Creature> movementFunction) {
        this.creatureId = 3;
        this.width = 260;
        this.height = 260;

        this.xPosition = spawnX;
        this.yPosition = spawnY;

        this.movementSpeedMultiplier = speedMultiplier;
        this.movementSpeed = BASE_SPEED * movementSpeedMultiplier;
        this.movementSpeed = shouldMoveLeft(spawnX) ? -movementSpeed : movementSpeed;
        this.movingLeft = shouldMoveLeft(spawnX) ? true : false;
        this.yVelocity = 0;

        this.movementFunction = movementFunction;
        this.spawnTime = spawnTime;
        this.isBouncable = false;

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
        /// Nothing can harm OceanSunfish
        //isBouncedOn = true;
    }
    @Override
    public void handleTimeStep(){
        float delta = Gdx.graphics.getDeltaTime();

        stateTime += delta;

        applyMovement();

        // START SHAKE BEFORE FLIP
        if (!isShaking && --flipCounter < 30) {
            isShaking = true;
            shakeTimer = SHAKE_DURATION;
            originalX = xPosition;
        }

        // HANDLE SHAKE
        if (isShaking) {

            shakeTimer -= delta;

            // jitter left/right
            xPosition = originalX + MathUtils.random(-SHAKE_INTENSITY, SHAKE_INTENSITY);

            if (shakeTimer <= 0f) {

                // restore exact position
                xPosition = originalX;

                isShaking = false;

                flip();
            }
        }

        updateBounds();
    }
    
    private void flip(){
        isBouncable = !isBouncable;
        flipCounter = defaultFlipCounter;

    }
    @Override
    public TextureRegion getAnimeFrame(){
        if(isBouncable){
            return oceanSunfishAnimationNormal.getKeyFrame(stateTime,true);
        }else{
            return oceanSunfishAnimationFlipped.getKeyFrame(stateTime,true);
        }

    }
    public static void initAnime(){
        TextureRegion[] frames = new TextureRegion[4];
        for(int i=0;i<4;i++){
            frames[i] = new TextureRegion(new Texture("creatures/sunfish/f"+i+".png"));
        }
        Texture texture = new Texture("creatures/sunfish/f_.png");
        TextureRegion[] frames_ = new TextureRegion[1];
        frames_[0] = new TextureRegion(texture);

        oceanSunfishAnimationNormal = new Animation<>(0.5f, frames_);
        oceanSunfishAnimationFlipped = new Animation<>(0.3f, frames);
    }

}
