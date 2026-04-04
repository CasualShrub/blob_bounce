package com.bouncefish.gameplay;

import com.bouncefish.entities.Creature;

import java.util.ArrayList;

// This class holds a list of creatures for each wave, as well as metadata about the wave itself that the spawner may need
public class Wave {
    private ArrayList<Creature> creatures;
    private float waveDuration;

    public Wave(float waveDuration) {
        this.creatures = new ArrayList<>();
        this.waveDuration = waveDuration;
    }

    public void add(Creature creature){
        this.creatures.add(creature);
    }

    public ArrayList<Creature> getCreatures(){
        return this.creatures;
    }

    public float getWaveDuration() {
        return this.waveDuration;
    }
    public void setWaveDuration(float waveDuration){
        this.waveDuration = waveDuration;
    }
}
