package com.bouncefish.entities;

import com.badlogic.gdx.Gdx;

public class Crab extends Creature {

    public Crab() {
        xPosition = 0;
        yPosition = 0;
        xVelocity = 0;
        yVelocity = 0;
        width = 130;
        height = 130;
        movementSpeed = 12;
        setBounds(); //Crabs have size and collision box
    }

    @Override
    public void handleTimeStep() {
        xPosition += movementSpeed;
        updateBounds(); // Their collision boxes move with them
    }

}
