package com.bouncefish.entities.spawndata;

import com.bouncefish.entities.Crab;
import com.bouncefish.entities.Creature;
import com.bouncefish.utils.GameConstants;

import java.util.function.Consumer;

public class CrabSpawnData extends CreatureSpawnData<Crab> {

    public CrabSpawnData(float spawnTime, float spawnX, Consumer<Creature> movementFunction){
        super(spawnTime, spawnX, GameConstants.WATER_LEVEL, 1, movementFunction);
    }

    public CrabSpawnData(float spawnTime, float spawnX, float spawnY, float speedMultiplier, Consumer<Creature> movementFunction) {
        super(spawnTime, spawnX, spawnY, speedMultiplier, movementFunction);
    }

    @Override
    public Crab createInstance() {
        return new Crab(this.spawnTime,
            this.spawnX,
            this.spawnY,
            this.speedMultiplier,
            this.movementFunction
        );
    }
}
