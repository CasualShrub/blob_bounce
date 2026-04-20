package com.bouncefish.entities.spawndata;

import com.bouncefish.entities.Crab;
import com.bouncefish.entities.Creature;
import com.bouncefish.entities.OceanSunfish;

import java.util.function.Consumer;

public class OceanSunfishSpawnData extends CreatureSpawnData<OceanSunfish> {

    public OceanSunfishSpawnData(float spawnTime, float spawnX, float spawnY, float speedMultiplier, Consumer<Creature> movementFunction) {
        super(spawnTime, spawnX, spawnY, speedMultiplier, movementFunction);
    }

    @Override
    public OceanSunfish createInstance() {
        return new OceanSunfish(this.spawnTime,
            this.spawnX,
            this.spawnY,
            this.speedMultiplier,
            this.movementFunction
        );
    }
}
