package com.bouncefish.gameplay;

import com.badlogic.gdx.math.MathUtils;
import com.bouncefish.entities.Crab;
import com.bouncefish.entities.JellyFish;
import com.bouncefish.entities.Mackerel;
import com.bouncefish.entities.OceanSunfish;
import com.bouncefish.entities.OrdinaryFish;
import com.bouncefish.entities.Shark;
import com.bouncefish.entities.spawndata.*;
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
    private static Dictionary<Integer, Wave> activeWaveList = new Hashtable<>();

    // Key: Wave Difficulty (Scales with points/time). 0 based
    // Values: ArrayList of wave data that the spawner can randomly choose from
    private static Dictionary<Integer, ArrayList<Wave>> waveData = new Hashtable<>();

    // This is where we define all wave data
    public static void populateWaveRegistry(){
        // Generate all waves??
        // TODO: consider batching them
        for (int i = 0; i < GameConstants.MAXIMUM_HARDCODED_WAVES; i++){
            activeWaveList.put(i, getRandomWaveForRound(i));
        }
    }

    // It is important to know the round before choosing from the pool
    private static Wave getRandomWaveForRound(int currentRound){
        int difficulty = getDifficultyForRound(currentRound);
        ArrayList<Wave> wavePool = waveData.get(difficulty);
        int waveIndex = MathUtils.random((wavePool.size() - 1)); // -1 Because GDX random is right-inclusive
        return wavePool.remove(waveIndex);
    }

    public static void generateWaves(){
        ArrayList<Wave> easyWaves = new ArrayList<>();

        // Java and LibGDX are code-centric, so we can't quite make visual ScriptableObjects like in Unity
        // Which means we manually define all wave data here!
        // TODO: Make a helper tool and pass a json file instead?

        Wave wave1 = new Wave(14f);
        wave1.add(new OceanSunfishSpawnData(1f,RIGHT_SPAWN_X,GameConstants.Screen_Height/2,1 ,OceanSunfish::normalMovement));
        wave1.add(new MackerelSpawnData(1f,GameConstants.Game_Width - 50F,GameConstants.Game_Width/2 -100, 1, OrdinaryFish::parabolicMotion));
        wave1.add(new MackerelSpawnData(1f,50F,GameConstants.Game_Width/2 + 100, 1, OrdinaryFish::parabolicMotion));
        wave1.add(new CrabSpawnData(1f, LEFT_SPAWN_X, Crab::normalMovement));
        wave1.add(new CrabSpawnData(2.5f, LEFT_SPAWN_X, Crab::normalMovement));
        wave1.add(new CrabSpawnData(5f, LEFT_SPAWN_X, Crab::normalMovement));
        wave1.add(new CrabSpawnData(6.8f, LEFT_SPAWN_X, Crab::normalMovement));
        wave1.add(new CrabSpawnData(8f, RIGHT_SPAWN_X, Crab::normalMovement));
        wave1.add(new CrabSpawnData(9f, LEFT_SPAWN_X, Crab::normalMovement));
        wave1.add(new CrabSpawnData(9.2f, RIGHT_SPAWN_X, Crab::normalMovement));
        wave1.add(new CrabSpawnData(10f, RIGHT_SPAWN_X, Crab::normalMovement));
        wave1.add(new SharkSpawnData(1F,RIGHT_SPAWN_X,true, 1, Shark::swim));
        wave1.add(new JellyfishSpawnData(1f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));
        wave1.add(new JellyfishSpawnData(1.5f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));//for testing
        wave1.add(new JellyfishSpawnData(2f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));//for testing
        wave1.add(new JellyfishSpawnData(2.5f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));//for testing
        wave1.add(new CrabSpawnData(12f, RIGHT_SPAWN_X, 0, 1.5f, Crab::normalMovement));
        easyWaves.add(wave1);

        Wave wave2 = new Wave(7f);
        wave2.add(new CrabSpawnData(1f, LEFT_SPAWN_X, 0, 1.5f, Crab::normalMovement));
        wave2.add(new CrabSpawnData(1.3f, RIGHT_SPAWN_X, 0, 1.5f, Crab::normalMovement));
        wave2.add(new CrabSpawnData(3f, LEFT_SPAWN_X, 0, 1.5f, Crab::normalMovement));
        wave2.add(new CrabSpawnData(3.3f, RIGHT_SPAWN_X, 0, 1.7f, Crab::normalMovement));
        wave2.add(new CrabSpawnData(5.7f, LEFT_SPAWN_X, 0, 1.5f, Crab::normalMovement));
        wave2.add(new CrabSpawnData(5f, RIGHT_SPAWN_X, 0, 1.3f, Crab::normalMovement));
        wave2.add(new CrabSpawnData(7f, LEFT_SPAWN_X, 0, 1.2f, Crab::normalMovement));
        wave2.add(new CrabSpawnData(6f, RIGHT_SPAWN_X, 0, 1.5f, Crab::normalMovement));
        wave2.add(new JellyfishSpawnData(1f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));
        wave2.add(new JellyfishSpawnData(1.5f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));//for testing
        wave2.add(new JellyfishSpawnData(2f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));//for testing
        wave2.add(new JellyfishSpawnData(2.5f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));//for testing
        easyWaves.add(wave2);

        Wave wave3 = new Wave(8f);
        wave3.add(new MackerelSpawnData(1f,LEFT_SPAWN_X, RIGHT_SPAWN_X, 1, OrdinaryFish::parabolicMotion));
        wave3.add(new MackerelSpawnData(2f,LEFT_SPAWN_X, RIGHT_SPAWN_X, 1, OrdinaryFish::parabolicMotion));
        wave3.add(new MackerelSpawnData(3f,LEFT_SPAWN_X, RIGHT_SPAWN_X, 1, OrdinaryFish::parabolicMotion));
        wave3.add(new MackerelSpawnData(4f,LEFT_SPAWN_X, RIGHT_SPAWN_X, 1, OrdinaryFish::parabolicMotion));
        wave3.add(new MackerelSpawnData(5f,LEFT_SPAWN_X, RIGHT_SPAWN_X, 1, OrdinaryFish::parabolicMotion));
        wave3.add(new MackerelSpawnData(6f,LEFT_SPAWN_X, RIGHT_SPAWN_X, 1, OrdinaryFish::parabolicMotion));
        wave3.add(new JellyfishSpawnData(1f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));
        wave3.add(new JellyfishSpawnData(1.5f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));//for testing
        wave3.add(new JellyfishSpawnData(2f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));//for testing
        wave3.add(new JellyfishSpawnData(2.5f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));//for testing
        wave3.add(new CrabSpawnData(7f, LEFT_SPAWN_X, 0, 1.2f, Crab::normalMovement));
        easyWaves.add(wave3);

        waveData.put(0, easyWaves);
    }

    // Converts current round to difficulty
    // TODO: Consider moving to a static helper class?
    private static int getDifficultyForRound(int currentWave){
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
    public static Wave getWave(int waveId){
        return activeWaveList.get(waveId);
    }

    public void populateEasyWaves(){

    }
}
