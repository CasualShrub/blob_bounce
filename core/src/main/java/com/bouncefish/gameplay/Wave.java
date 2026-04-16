package com.bouncefish.gameplay;
import com.bouncefish.entities.spawndata.CreatureSpawnData;

import java.util.ArrayList;

// This class holds a list of creatures for each wave, as well as metadata about the wave itself that the spawner may need
public class Wave {
    private final ArrayList<CreatureSpawnData<?>> creatureData;
    private float waveDuration;

    public Wave(float waveDuration) {
        this.creatureData = new ArrayList<>();
        this.waveDuration = waveDuration;
    }

    public void add(CreatureSpawnData<?> creature){
        this.creatureData.add(creature);
    }

    public float getWaveDuration() {
        return this.waveDuration;
    }
    public void setWaveDuration(float waveDuration){
        this.waveDuration = waveDuration;
    }
    public ArrayList<CreatureSpawnData<?>> getCreaturesToSpawn(){
        return this.creatureData;
    }
}
