package com.bouncefish.entities.spawndata;
import com.bouncefish.entities.Creature;
import com.bouncefish.entities.Mackerel;

import java.util.function.Consumer;

public class MackerelSpawnData extends CreatureSpawnData<Mackerel> {

    // Mackerel needs to know where it will end to calculate movement
    private float endX;

    public MackerelSpawnData(float spawnTime, float spawnX, float endX, float speedMultiplier, Consumer<Creature> movementFunction) {
        //Mackerel
        super(spawnTime, spawnX, -50, speedMultiplier, movementFunction);
        this.endX = endX;
    }

    @Override
    public Mackerel createInstance() {
        return new Mackerel(this.spawnTime,
            this.spawnX,
            this.endX,
            this.speedMultiplier,
            this.movementFunction
        );
    }
}
