package com.bouncefish.entities;

import com.badlogic.gdx.Gdx;

public class Creature {
    protected double xPosition;
    protected double yPosition;
    protected double width;
    protected double height;
    protected double xVelocity;
    protected double yVelocity;

    public void updatePosition(){
        xPosition += xVelocity * Gdx.graphics.getDeltaTime();
        yPosition += yVelocity * Gdx.graphics.getDeltaTime();
    }

    public double getX() {
        return xPosition;
    }

    public void setX(double x) {
        xPosition = x;
    }

    public double getY() {
        return yPosition;
    }

    public void setY(double y) {
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
}
