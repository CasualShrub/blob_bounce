package com.bouncefish.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bouncefish.utils.GameConstants;

public abstract class OrdinaryFish extends Creature{
    protected static float leftStartX = 50F;
    protected static float rightStartX = GameConstants.Game_Width - 50F;
    protected static float startY = 0F;
    protected static float leftEndX = GameConstants.Game_Width/2 -100;
    protected static float rightEndX = GameConstants.Game_Width/2 + 100;

    protected static float apexY = GameConstants.Screen_Height/6; // maximum height of the parabola
    @Override
    public void handleBouncedOn() { //ordinary fish all behave the same; so put this here

    }
    public static void leftToRightParabola(Creature creature){
        parabolicMotion(creature,true);
    }
    public static void rightToLeftParabola(Creature creature){
        //TODO: flip the texture.
        parabolicMotion(creature,false);
    }

    private static void parabolicMotion(Creature creature, boolean goRight){//ordinary fish all behave the same; so put this here
        float deltaTime = Gdx.graphics.getDeltaTime();
        float start_X = goRight ? leftStartX : rightStartX;
        float end_X = goRight ? leftEndX : rightEndX;

        //y = a*x^2 + b*x + c
        float c = startY;
        float b = 4 * (apexY - startY) / (end_X - start_X);
        float a = -b / (2 * (end_X - start_X));

        creature.xPosition += creature.xVelocity * deltaTime;
        float dx = creature.xPosition - start_X;

        creature.yPosition = a * dx * dx + b * dx + c;
    }
    //TODO: add methods to change the parameters of the parabolic motion?



}
