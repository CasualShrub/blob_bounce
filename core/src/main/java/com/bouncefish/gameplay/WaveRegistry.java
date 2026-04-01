package com.bouncefish.gameplay;

import com.bouncefish.entities.Crab;
import com.bouncefish.entities.JellyFish;
import com.bouncefish.utils.GameConstants;

import java.util.Dictionary;
import java.util.Hashtable;

// This class holds all the data of enemies that will spawn in the game
public class WaveRegistry {
    private Dictionary<Integer, Wave> _waveList;
    private static final float LEFT_SPAWN_X = -200;
    private static float RIGHT_SPAWN_X = GameConstants.Game_Width;
    public WaveRegistry() {
        _waveList = new Hashtable<>();
        populateWaveRegistry();
    }

    // This is where we define all wave data
    private void populateWaveRegistry(){
        if (_waveList == null) {
            return;
        }
        // Java and LibGDX are code-centric, so we can't quite make visual ScriptableObjects like in Unity
        // Which means we manually define all wave data here!
        Wave wave1 = new Wave();
        wave1.add(new Crab(1f,LEFT_SPAWN_X,0, 1, Crab::crabMovementLeftToRight));
        wave1.add(new Crab(2f, LEFT_SPAWN_X,0, 1, Crab::crabMovementLeftToRight));
        wave1.add(new Crab(2.5f, LEFT_SPAWN_X,0, 1, Crab::crabMovementLeftToRight));
        wave1.add(new Crab(3f, LEFT_SPAWN_X,0, 1, Crab::crabMovementLeftToRight));
        wave1.add(new Crab(3f, RIGHT_SPAWN_X,0, 1, Crab::crabMovementRightToLeft));
        wave1.add(new Crab(4f, LEFT_SPAWN_X,0, 1, Crab::crabMovementLeftToRight));
        wave1.add(new JellyFish(4f, LEFT_SPAWN_X, 300, 1, JellyFish::jellyFishLeftToRightMovement));
        _waveList.put(1, wave1);
    }

    // Generate 5 endless waves at a time once the player reaches difficulty cap
    private void generateEndlessWaves(){

    }

    public Wave getWave(int waveId){
        return _waveList.get(waveId);
    }
}
