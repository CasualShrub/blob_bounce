package com.bouncefish.entities.spawndata;

import com.bouncefish.entities.Creature;

import java.util.function.Consumer;

public abstract class CreatureSpawnData<T extends Creature> {
    protected float spawnTime;
    protected float spawnX;
    protected float spawnY;
    protected Consumer<Creature> movementFunction;
    protected float speedMultiplier;

    public CreatureSpawnData(float spawnTime, float spawnX, float spawnY, float speedMultiplier, Consumer<Creature> movementFunction){
        this.spawnTime = spawnTime;
        this.spawnX = spawnX;
        this.spawnY = spawnY;
        this.speedMultiplier = speedMultiplier;
        this.movementFunction = movementFunction;
    }

    public abstract T createInstance();

    public float getSpawnTime() {
        return this.spawnTime;
    }

    public float getSpawnX() {
        return this.spawnX;
    }

    public float getSpawnY() {
        return this.spawnY;
    }

    public float getSpeedMultiplier() {
        return this.speedMultiplier;
    }

    public Consumer<Creature> getMovementFunction() {
        return this.movementFunction;
    }
}
