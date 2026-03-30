package com.bouncefish.gameplay;

import com.bouncefish.entities.Creature;

import java.util.ArrayList;

//This class defines what a wave is
public class Wave {
    ArrayList<Creature> _creatures;
    double _speedMultiplier;

    public Wave() {
        _creatures = new ArrayList<Creature>();
    }

    public void add(Creature creature) {
        _creatures.add(creature);
    }
}
