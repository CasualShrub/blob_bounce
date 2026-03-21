package com.bouncefish.entities;

import com.badlogic.gdx.Gdx;

public class Crab extends Creature {

    public Crab() {
        xPosition = 0;
        yPosition = 0;
        xVelocity = 0;
        yVelocity = 0;
        width = 200;
        height = 200;
        movementSpeed = 12;
    }

    @Override
    public void handleTimeStep() {
        xPosition += movementSpeed;
    }

}
