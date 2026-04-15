package com.bouncefish.gameplay;

import com.badlogic.gdx.math.MathUtils;
import com.bouncefish.entities.Crab;
import com.bouncefish.entities.JellyFish;
import com.bouncefish.entities.Mackerel;
import com.bouncefish.entities.OceanSunfish;
import com.bouncefish.entities.OrdinaryFish;
import com.bouncefish.entities.Shark;
import com.bouncefish.utils.GameConstants;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;

// This class holds all the data of enemies that will spawn in the game
public class WaveRegistry {
    private static final float LEFT_SPAWN_X = -200;
    private static float RIGHT_SPAWN_X = GameConstants.Game_Width;

    // Key: Round to spawn waves. 0 based
    // Value: Wave Data
    private Dictionary<Integer, Wave> activeWaveList;

    // Key: Wave Difficulty (Scales with points/time). 0 based
    // Values: ArrayList of wave data that the spawner can randomly choose from
    private Dictionary<Integer, ArrayList<Wave>> waveData;
    public WaveRegistry() {
        this.activeWaveList = new Hashtable<>();
        this.waveData = new Hashtable<>();
        generateWaves();
        populateWaveRegistry();
    }

    // This is where we define all wave data
    private void populateWaveRegistry(){
        if (this.waveData == null) {
            return;
        }

        if (this.activeWaveList == null){
            return;
        }

        // Generate all waves??
        // TODO: consider batching them
        for (int i = 0; i < GameConstants.MAXIMUM_HARDCODED_WAVES; i++){
            this.activeWaveList.put(i, getRandomWaveForRound(i));
        }
    }

    // It is important to know the round before choosing from the pool
    private Wave getRandomWaveForRound(int currentRound){
        int difficulty = getDifficultyForRound(currentRound);
        ArrayList<Wave> wavePool = this.waveData.get(difficulty);
        int waveIndex = MathUtils.random((wavePool.size() - 1)); // -1 Because GDX random is right-inclusive
        return wavePool.remove(waveIndex);
    }

    private void generateWaves(){
        ArrayList<Wave> easyWaves = new ArrayList<>();

        // Java and LibGDX are code-centric, so we can't quite make visual ScriptableObjects like in Unity
        // Which means we manually define all wave data here!
        // TODO: Make a helper tool and pass a json file instead?

        Wave wave1 = new Wave(14f);
        wave1.add(new OceanSunfish(1f,RIGHT_SPAWN_X,GameConstants.Screen_Height/2,true,OceanSunfish::OceanSunfishMovementRightToLeft));
        wave1.add(new Mackerel(1f,GameConstants.Game_Width - 50F,GameConstants.Game_Width/2 -100, OrdinaryFish::leftToRightParabola));
        wave1.add(new Mackerel(1f,50F,GameConstants.Game_Width/2 + 100, OrdinaryFish::rightToLeftParabola));
        wave1.add(new Crab(1f, LEFT_SPAWN_X, Crab::leftToRight));
        wave1.add(new Crab(2.5f, LEFT_SPAWN_X, Crab::leftToRight));
        wave1.add(new Crab(5f, LEFT_SPAWN_X, Crab::leftToRight));
        wave1.add(new Crab(6.8f, LEFT_SPAWN_X, Crab::leftToRight));
        wave1.add(new Crab(8f, RIGHT_SPAWN_X, Crab::rightToLeft));
        wave1.add(new Crab(9f, LEFT_SPAWN_X, Crab::leftToRight));
        wave1.add(new Crab(9.2f, RIGHT_SPAWN_X, Crab::rightToLeft));
        wave1.add(new Crab(10f, RIGHT_SPAWN_X, Crab::rightToLeft));
        wave1.add(new Shark(1F,RIGHT_SPAWN_X,true,Shark::swim));
        //wave1.add(new JellyFish(3f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));
        wave1.add(new Crab(12f, RIGHT_SPAWN_X, 0, 1.5f, Crab::rightToLeft));
        easyWaves.add(wave1);

        Wave wave2 = new Wave(7f);
        wave2.add(new Crab(1f, LEFT_SPAWN_X, 0, 1.5f, Crab::leftToRight));
        wave2.add(new Crab(1.3f, RIGHT_SPAWN_X, 0, 1.5f, Crab::rightToLeft));
        wave2.add(new Crab(3f, LEFT_SPAWN_X, 0, 1.5f, Crab::leftToRight));
        wave2.add(new Crab(3.3f, RIGHT_SPAWN_X, 0, 1.7f, Crab::rightToLeft));
        wave2.add(new Crab(5.7f, LEFT_SPAWN_X, 0, 1.5f, Crab::leftToRight));
        wave2.add(new Crab(5f, RIGHT_SPAWN_X, 0, 1.3f, Crab::rightToLeft));
        wave2.add(new Crab(7f, LEFT_SPAWN_X, 0, 1.2f, Crab::leftToRight));
        wave2.add(new Crab(6f, RIGHT_SPAWN_X, 0, 1.5f, Crab::rightToLeft));
        easyWaves.add(wave2);

        Wave wave3 = new Wave(8f);
        //wave3.add(new Mackerel(1f,true, OrdinaryFish::leftToRightParabola));
        //wave3.add(new Mackerel(2f,false, OrdinaryFish::rightToLeftParabola));
        //wave3.add(new Mackerel(3f,true, OrdinaryFish::leftToRightParabola));
        //wave3.add(new Mackerel(4f,false, OrdinaryFish::rightToLeftParabola));
        //wave3.add(new Mackerel(5f,true, OrdinaryFish::leftToRightParabola));
       //wave3.add(new Mackerel(6f,false, OrdinaryFish::rightToLeftParabola));
        wave3.add(new Crab(7f, LEFT_SPAWN_X, 0, 1.2f, Crab::leftToRight));
        easyWaves.add(wave3);

        this.waveData.put(0, easyWaves);
    }

    // Converts current round to difficulty
    // TODO: Consider moving to a static helper class?
    private int getDifficultyForRound(int currentWave){
        // If current wave is 5 or less, then it is an easy wave.
        if (currentWave <= 5) {
            return 0;
        }

        return 0;
    }

    // Generate 5 endless waves at a time once the player reaches difficulty cap
    private void generateEndlessWaves(){

    }

    // Used exclusively by the CreatureSpawner to generate the current wave
    public Wave getWave(int waveId){
        return this.activeWaveList.get(waveId);
    }

    public void populateEasyWaves(){

    }
}
