package com.bouncefish.gameplay;

import com.bouncefish.entities.Creature;

import java.util.ArrayList;

// This class is responsible for handling the spawn timing and types of creatures based on game time / points
public class CreatureSpawner {
    private ArrayList<Creature> _creatureList;
    private WaveRegistry _waveRegistry;

    public CreatureSpawner() {
        _creatureList = new ArrayList<Creature>();
        _waveRegistry = new WaveRegistry();

        //Populate creatureList with the first wave
    }

    public ArrayList<Creature> spawnWave(int waveId) {
        return null;
    }


}
