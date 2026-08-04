package com.bouncefish.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.bouncefish.utils.GameConstants;

import java.util.function.Consumer;

public class Mackerel extends OrdinaryFish{
    private static TextureRegion exitingFrame;
    private static TextureRegion swimmingFrame;
    private static TextureRegion deadFrame;
    private static Animation<TextureRegion> enteringAnimation;
    private final int BASE_SPEED = 700;
    public Mackerel(float spawnTime, float startX, float endX, float speedMultiplier, Consumer<Creature> movementFunction) {
        super(startX,endX);
        this.creatureId = 4;
        this.width = 170;
        this.height = 170;
        this.boundOffsetY = this.getHeight() * 0.25f;

        this.movementSpeedMultiplier = speedMultiplier;
        this.xVelocity = movingLeft ? -BASE_SPEED : BASE_SPEED;
        this.xVelocity *= movementSpeedMultiplier;

        this.movementFunction = movementFunction;
        this.spawnTime = spawnTime;

        setBounds();
    }

    @Override
    protected void setBounds(){
        this.bounds = new Rectangle(getX(), getY() + this.boundOffsetY, getWidth(), getHeight() * 0.5f);
    }
    public static void initAnime(){
        exitingFrame  = new TextureRegion(new Texture("creatures/fish/f0.png"));
        swimmingFrame = new TextureRegion(new Texture("creatures/fish/f1.png"));
        deadFrame     = new TextureRegion(new Texture("creatures/fish/dead.png"));
        TextureRegion[] enterFrames = new TextureRegion[]{
            new TextureRegion(new Texture("creatures/fish/f2.png")),
            new TextureRegion(new Texture("creatures/fish/f3.png"))
        };
        enteringAnimation = new Animation<>(0.12F, enterFrames);
    }
    @Override
    public boolean shouldFlipHorizontally() {
        return !isMovingLeft();
    }

    @Override
    public float getRenderWidth() {
        TextureRegion frame = getAnimeFrame();
        float aspect = frame.getRegionWidth() / (float) frame.getRegionHeight();
        return getHeight() * aspect;
    }

    @Override
    public float getRenderXOffset() {
        return (getWidth() - getRenderWidth()) / 2f;
    }

    @Override
    public TextureRegion getAnimeFrame() {
        if (isBouncedOn) return deadFrame;
        switch (getFishState()) {
            case SWIMMING: return swimmingFrame;
            case ENTERING: return enteringAnimation.getKeyFrame(getEnteringStateTime(), false);
            case EXITING:
            default:       return exitingFrame;
        }
    }
}
