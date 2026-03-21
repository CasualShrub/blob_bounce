package com.bouncefish.entities;

import com.badlogic.gdx.Gdx;

public abstract class Creature {
    protected float xPosition;
    protected float yPosition;
    protected int width;
    protected int height;
    protected double xVelocity;
    protected double yVelocity;
    protected float movementSpeed;

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
        this.yVelocity -= yVelocity;
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
    }

    public void spawn(float x, float y) {
        xPosition = x;
        yPosition = y;
    }

    public abstract void handleTimeStep();
}
