package com.bouncefish.entities.spawndata;

import com.bouncefish.entities.Marlin;
import com.bouncefish.entities.Creature;
import com.bouncefish.utils.GameConstants;

import java.util.function.Consumer;

public class MarlinSpawnData extends CreatureSpawnData<Marlin>{
    public MarlinSpawnData(float spawnTime, float spawnX, float spawnY, float speedMultiplier, Consumer<Creature> movementFunction) {
        super(spawnTime, spawnX, spawnY, speedMultiplier, movementFunction);
    }

    @Override
    public Marlin createInstance() {
        return new Marlin(this.spawnTime,
            this.spawnX,
            this.spawnY,
            this.speedMultiplier,
            this.movementFunction
        );
    }
}
