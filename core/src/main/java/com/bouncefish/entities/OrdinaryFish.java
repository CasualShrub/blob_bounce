package com.bouncefish.entities;

import com.badlogic.gdx.Gdx;
import com.bouncefish.utils.GameConstants;

public abstract class OrdinaryFish extends Creature{
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
    protected static float apexY = GameConstants.Screen_Height/6; // maximum height of the parabola

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
        b = 4 * (apexY - c) / (end_X - start_X);
        a = -b / (2 * (end_X - start_X));
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

    }
    public static void leftToRightParabola(Creature creature){
        parabolicMotion(creature);
    }
    public static void rightToLeftParabola(Creature creature){
        parabolicMotion(creature);
    }

    private static void parabolicMotion(Creature creature){
        float deltaTime = Gdx.graphics.getDeltaTime();

        creature.xPosition += creature.xVelocity * deltaTime;
        float dx = creature.xPosition - creature.getStartX();

        creature.yPosition = creature.getLeadingCoefficient() * dx * dx + creature.getLinearCoefficient() * dx + c;
    }
    //TODO: add methods to change the parameters of the parabolic motion?



}
