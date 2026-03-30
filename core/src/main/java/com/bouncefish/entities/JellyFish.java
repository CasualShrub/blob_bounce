package com.bouncefish.entities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

public class JellyFish extends Creature {
    //y = scale * sin( angularFreq * x), just a reminder
    private float angularFrequency = 0.1F;
    private float scale = 500F;
    public JellyFish(){
        xPosition = 0;
        yPosition = 0;
        xVelocity = 0;
        yVelocity = 0;
        width = 120;
        height = 120;
        movementSpeed = 0;
        //Textures= new Texture("crab1.png");
        setBounds();
    }

    @Override
    public void updatePosition(){
        xPosition += xVelocity * Gdx.graphics.getDeltaTime();
        setYVelocity(scale * MathUtils.cos(angularFrequency * xPosition));
        yPosition += yVelocity * Gdx.graphics.getDeltaTime();
    }

    @Override
    public void handleTimeStep() {
        updatePosition();
        updateBounds();
    }

    public void setAngularFrequency(float newFre){
        angularFrequency = newFre;
    }

    public void setScale(int newScale){
        scale = newScale;
    }
}
