package com.bouncefish.entities;

import com.badlogic.gdx.Gdx;

import java.util.function.Consumer;

public class Crab extends Creature {

    public Crab(float spawnTime, float spawnX, float spawnY, float speedMultiplier, Consumer<Creature> movementFunction) {
        creatureId = 1;
        xVelocity = 0;
        yVelocity = 0;
        width = 130;
        height = 130;
        movementSpeed = 12;

        this.xPosition = spawnX;
        this.yPosition = spawnY;
        this.movementSpeedMultiplier = speedMultiplier;
        this.movementFunction = movementFunction;
        this.spawnTime = spawnTime;

        setBounds();
    }

    // This constructor is used if we just want a default crab (Default spawn y is 0, speedMultiplier of 1)
    public Crab(float spawnTime, float spawnX, Consumer<Creature> movementFunction) {
        creatureId = 1;
        xVelocity = 0;
        yVelocity = 0;
        width = 130;
        height = 130;
        movementSpeed = 500;

        this.xPosition = spawnX;
        this.yPosition = 0;
        this.movementSpeedMultiplier = 1;
        this.movementFunction = movementFunction;
        this.spawnTime = spawnTime;

        setBounds();
    }

    public static void leftToRight(Creature creature){
        creature.xPosition += creature.movementSpeed * creature.movementSpeedMultiplier * Gdx.graphics.getDeltaTime();
    }

    public static void rightToLeft(Creature creature){
        creature.xPosition -= creature.movementSpeed * creature.movementSpeedMultiplier * Gdx.graphics.getDeltaTime();
    }

    @Override
    public void handleBouncedOn() {
        isDead = true;
    }
}
