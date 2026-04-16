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
    protected static float c = 0F;
    protected static float apexY = GameConstants.Screen_Height/6; // maximum height of the parabola
    protected float x1;
    protected float x2; //the parabola is y=a(x-x1)(x-x2); assume x2 > x1
    protected float h;

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
    OrdinaryFish(float start_X, float end_X, float h){
        if(start_X < end_X){
            this.movingLeft = false;
        }else {
            this.movingLeft = true;
        }
        this.x1 = Math.min(start_X, end_X);
        this.x2 = Math.max(start_X,end_X);
        this.h =h;
        this.xPosition = start_X;
        this.yPosition = 0;
        this.a = -4*h/((x1-x2)*(x1-x2));
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
    public float getParabolicMovementMaxHeight(){
        return h;
    }
    @Override
    public float getParabolicMovementLeftEnd(){
        return x1;
    }
    @Override
    public float getParabolicMovementRightEnd(){
        return x2;
    }
    @Override
    public void handleBouncedOn() { //ordinary fish all behave the same; so put this here

    }
    public static void leftToRightParabola(Creature creature){
        //parabolicMotion(creature);
        parabolicMovement(creature);
        if(creature.xPosition > GameConstants.RIGHT_DEALLOCATE_X) creature.deactivate();
    }
    public static void rightToLeftParabola(Creature creature){

        //parabolicMotion(creature);
        parabolicMovement(creature);
        if(creature.xPosition < GameConstants.LEFT_DEALLOCATE_X) creature.deactivate();
    }

    private static void parabolicMotion(Creature creature){
        float deltaTime = Gdx.graphics.getDeltaTime();

        creature.xPosition += creature.xVelocity * deltaTime;
        float dx = creature.xPosition - creature.getStartX();

        creature.yPosition = creature.getLeadingCoefficient() * dx * dx + creature.getLinearCoefficient() * dx;
        if(creature.yPosition < GameConstants.BELOW_DEALLOCATE_Y){creature.deactivate();}
    }
    private static void parabolicMovement(Creature creature){
        float deltaTime = Gdx.graphics.getDeltaTime();
        float deltaX = creature.xVelocity * deltaTime;
        creature.xPosition += deltaX;
        /*float deltaY = 4*creature.getParabolicMovementMaxHeight()* deltaX/(creature.getParabolicMovementRightEnd()- creature.getParabolicMovementLeftEnd());
        if(creature.movingLeft){deltaY = -deltaY;}
        creature.yPosition +=deltaY;
         */
        float x1 = creature.getParabolicMovementLeftEnd();
        float x2 = creature.getParabolicMovementRightEnd();
        float x = creature.getX();
        float a = creature.getLeadingCoefficient();
        creature.setY(a*(x-x1)*(x-x2));
        if(creature.yPosition < GameConstants.BELOW_DEALLOCATE_Y){creature.deactivate();}
    }

}
