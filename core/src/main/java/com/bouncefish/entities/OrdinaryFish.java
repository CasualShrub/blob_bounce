package com.bouncefish.entities;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.bouncefish.utils.GameConstants;

public abstract class OrdinaryFish extends Creature{
    public enum FishState { EXITING, SWIMMING, ENTERING }

    /*protected static float leftStartX = 50F;
    protected static float rightStartX = GameConstants.Game_Width - 50F;

    protected static float leftEndX = GameConstants.Game_Width/2 -100;
    protected static float rightEndX = GameConstants.Game_Width/2 + 100;

     */
    protected float start_X;
    protected float end_X;
    //parameters for parabolic motion; y = a*x^2 + b*x + c
    protected float a;
    protected float b;
    protected static final float c = 0F;
    protected static float apexY = GameConstants.Screen_Height/5; // maximum height of the parabola

    protected FishState fishState = FishState.EXITING;
    protected float enteringStateTime = 0f;

    public FishState getFishState() { return fishState; }
    public float getEnteringStateTime() { return enteringStateTime; }

    OrdinaryFish(float start_X, float end_X){
        if(start_X < end_X){
            this.movingLeft = false;
        }else {
            this.movingLeft = true;
        }
        this.start_X = start_X;
        this.xPosition = start_X;
        this.yPosition = c;
        this.end_X = end_X;

        float totalWidth = Math.abs(end_X - start_X);
        b = (4 * apexY) / totalWidth;
        a = (-4 * apexY) / (totalWidth * totalWidth);
    }
    @Override
    public float getStartX(){
        return start_X;
    }
    @Override
    public float getLeadingCoefficient(){
        return a;
    }
    @Override
    public float getLinearCoefficient(){
        return b;
    }
    @Override
    public void handleBouncedOn() { //ordinary fish all behave the same; so put this here
        isBouncedOn = true;
        isBouncable = false;
        inWater = false;
        float newXVelocity = movingLeft? -50:50;
        setXVelocity(newXVelocity);
        setYVelocity(-800);
        setMovementFunction(OrdinaryFish::bouncedOnMovement);
    }

    public static void bouncedOnMovement(Creature creature){
        creature.xPosition += creature.xVelocity * creature.movementSpeedMultiplier * Gdx.graphics.getDeltaTime();
        creature.yPosition += creature.yVelocity * creature.movementSpeedMultiplier * Gdx.graphics.getDeltaTime();
        if(!creature.inWater && creature.yPosition<=GameConstants.WATER_LEVEL){
            creature.inWater = true;
            Water.playSplash(creature.xPosition);
        }
    }

    public static void parabolicMotion(Creature creature){
        float deltaTime = Gdx.graphics.getDeltaTime();

        creature.xPosition += creature.getXVelocity() * deltaTime;
        float dx = Math.abs(creature.getX() - creature.getStartX());

        float lastYPosition = creature.yPosition;
        creature.yPosition = (creature.getLeadingCoefficient() * dx * dx) + (creature.getLinearCoefficient() * dx) + c;
        float dy = creature.yPosition - lastYPosition;

        if (creature instanceof OrdinaryFish) {
            OrdinaryFish fish = (OrdinaryFish) creature;
            FishState prev = fish.fishState;
            float apexBand = apexY * 0.7f;
            if (creature.yPosition >= apexBand) {
                fish.fishState = FishState.SWIMMING;
            } else if (dy > 0f) {
                fish.fishState = FishState.EXITING;
            } else {
                fish.fishState = FishState.ENTERING;
            }
            if (fish.fishState == FishState.ENTERING) {
                if (prev != FishState.ENTERING) fish.enteringStateTime = 0f;
                fish.enteringStateTime += deltaTime;
            }
        }

        if(!creature.jumpedOutOfWater && creature.yPosition - GameConstants.WATER_LEVEL <= 0.1F){ //play the splash near the position where the fish jumps out of the water
            creature.jumpedOutOfWater = true;
            Water.playSplash(creature.xPosition);
        }else if(dy < 0 && !creature.inWater && creature.yPosition<=GameConstants.WATER_LEVEL){
            creature.inWater = true;
            int xOffset = creature.movingLeft? -75:75; //magic number to fix the position of the splash
            Water.playSplash(creature.xPosition+xOffset);
        }

    }
}
