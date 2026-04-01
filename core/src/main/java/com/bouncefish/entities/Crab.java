package com.bouncefish.entities;

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

    public static void crabMovementLeftToRight(Creature creature){
        creature.xPosition += creature.movementSpeed;
    }

    public static void crabMovementRightToLeft(Creature creature){
        creature.xPosition -= creature.movementSpeed;
    }

    @Override
    public void handleBouncedOn() {
        isDead = true;
    }
}
