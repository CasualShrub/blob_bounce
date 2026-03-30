package com.bouncefish.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

public abstract class Creature {
    protected float xPosition;
    protected float yPosition;
    protected int width;
    protected int height;
    protected double xVelocity;
    protected double yVelocity;
    protected float movementSpeed;
    protected Rectangle bounds; // Every object now has hitbox

    public float getX() {
        return xPosition;
    }

    public void setX(float x) {
        xPosition = x;
    }

    public float getY() {
        return yPosition;
    }

    public void setY(float y) {
        yPosition = y;
    }

    // (Sample reference template for methods with return values)
    /** @return The current horizontal velocity of the creature. */
    public double getXVelocity(){
        return xVelocity;
    }

    public void setXVelocity(double xVelocity) {
        this.xVelocity = xVelocity;
    }

    public void incrementXVelocity(double xVelocity) {
        this.xVelocity += xVelocity;
    }

    public void decrementXVelocity(double xVelocity) {
        this.xVelocity -= xVelocity;
    }

    public double getYVelocity(){
        return yVelocity;
    }

    public void setYVelocity(double yVelocity) {
        this.yVelocity = yVelocity;
    }

    public void incrementYVelocity(double yVelocity) {
        this.yVelocity += yVelocity;
    }

    public void decrementYVelocity(double yVelocity) {
        this.yVelocity -= yVelocity * Gdx.graphics.getDeltaTime();
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // Game loop
    public void updatePosition(){
        xPosition += xVelocity * Gdx.graphics.getDeltaTime();
        yPosition += yVelocity * Gdx.graphics.getDeltaTime();
        //Now Movement is Frame-rate independent.
    }

    public void spawn(float x, float y) {
        xPosition = x;
        yPosition = y;
    }
    //Created collision box
    public void setBounds(){
        bounds = new Rectangle((int)getX(), (int)getY(),
            getWidth(), getHeight());
    }

    //Keeps box synced with movement
    //Every entity is rectangle in space
    public void updateBounds(){
        bounds.setX((int)getX());
        bounds.setY((int)getY());
    }


    public abstract void handleTimeStep();
}
