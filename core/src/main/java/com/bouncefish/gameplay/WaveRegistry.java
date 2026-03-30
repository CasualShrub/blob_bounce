package com.bouncefish.gameplay;

import java.util.ArrayList;
import java.util.Dictionary;

public class WaveRegistry {
    private Dictionary<Integer, Wave> _waveList;

    public WaveRegistry() {
        populateWaveRegistry();
    }

    // This is where we define all wave data
    private void populateWaveRegistry(){
        // Java and LibGDX are code-centric, so we can't quite make visual ScriptableObjects like in Unity
        // Which means we manually define all wave data here!
        Wave wave1 = new Wave();
    }

    // Generate 5 endless waves at a time once the player reaches difficulty cap
    private void generateEndlessWaves(){

    }

    public void getWave(int waveId){
        _waveList.get(waveId);
    }
}
