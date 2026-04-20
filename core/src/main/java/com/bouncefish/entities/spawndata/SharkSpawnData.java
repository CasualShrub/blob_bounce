package com.bouncefish.entities.spawndata;

import com.bouncefish.entities.Creature;
import com.bouncefish.entities.Shark;
import com.bouncefish.utils.GameConstants;

import java.util.function.Consumer;

public class SharkSpawnData extends CreatureSpawnData<Shark> {

    // The shark can spawn somewhere in the center of the map, so we should also set his direction
    private final boolean isMovingLeft;

    public SharkSpawnData(float spawnTime, float spawnX, boolean isMovingLeft, float speedMultiplier, Consumer<Creature> movementFunction) {
        super(spawnTime, spawnX, GameConstants.WATER_LEVEL, speedMultiplier, movementFunction);
        this.isMovingLeft = isMovingLeft;
    }

    @Override
    public Shark createInstance() {
        return new Shark(this.spawnTime,
            this.spawnX,
            this.isMovingLeft,
            this.movementFunction
        );
    }
}
