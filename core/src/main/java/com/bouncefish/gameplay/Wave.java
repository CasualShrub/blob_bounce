package com.bouncefish.gameplay;

import com.bouncefish.entities.Creature;

import java.util.ArrayList;

// This class holds a list of creatures for each wave, as well as metadata about the wave itself that the spawner may need
public class Wave {
    private ArrayList<Creature> creatures;

    public Wave() {
        this.creatures = new ArrayList<>();
    }

    public void add(Creature creature){
        this.creatures.add(creature);
    }

    public ArrayList<Creature> getCreatures(){
        return this.creatures;
    }
}
