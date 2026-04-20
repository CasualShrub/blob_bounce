package com.bouncefish.entities.spawndata;

import com.bouncefish.entities.Creature;
import com.bouncefish.entities.JellyFish;

import java.util.function.Consumer;

public class JellyfishSpawnData extends CreatureSpawnData<JellyFish> {

    public JellyfishSpawnData(float spawnTime, float spawnX, float spawnY, float speedMultiplier, Consumer<Creature> movementFunction) {
        super(spawnTime, spawnX, spawnY, speedMultiplier, movementFunction);
    }

    @Override
    public JellyFish createInstance() {
        return new JellyFish(this.spawnTime,
            this.spawnX,
            this.spawnY,
            this.speedMultiplier,
            this.movementFunction
        );
    }
}
