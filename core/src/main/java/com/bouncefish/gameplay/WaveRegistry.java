package com.bouncefish.gameplay;

import com.badlogic.gdx.math.MathUtils;
import com.bouncefish.entities.Crab;
import com.bouncefish.entities.JellyFish;
import com.bouncefish.entities.Mackerel;
import com.bouncefish.entities.Marlin;
import com.bouncefish.entities.OceanSunfish;
import com.bouncefish.entities.OrdinaryFish;
import com.bouncefish.entities.Shark;
import com.bouncefish.entities.spawndata.*;
import com.bouncefish.utils.GameConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WaveRegistry {
    private static final float LEFT_SPAWN_X = -300;
    private static float RIGHT_SPAWN_X = GameConstants.Game_Width + 100;

    private static Map<Integer, Wave> activeWaveList = new HashMap<>();
    private static Map<Integer, ArrayList<Wave>> waveData = new HashMap<>();

    public static void prepareInitialWaves(){
        for (int i = 0; i < GameConstants.MAXIMUM_HARDCODED_WAVES; i++){
            activeWaveList.put(i, getRandomWaveForRound(i));
        }
    }

    private static Wave getRandomWaveForRound(int currentRound){
        int difficulty = getDifficultyForRound(currentRound);
        ArrayList<Wave> wavePool = waveData.get(difficulty);
        int waveIndex = MathUtils.random((wavePool.size() - 1));
        return wavePool.get(waveIndex);
    }

    public static void resetWaveRegistry(){
        activeWaveList.clear();
        waveData.clear();
    }

    public static void generateWaves(){
        ArrayList<Wave> easyWaves = new ArrayList<>();

        // Wave 1: Introduction to Marlin (The fast fish)
        Wave wave1 = new Wave(9f);
        wave1.add(new MarlinSpawnData(2f, LEFT_SPAWN_X, Marlin::normalMovement)); // Fast Marlin early on
        wave1.add(new CrabSpawnData(4f, RIGHT_SPAWN_X, Crab::normalMovement));
        wave1.add(new MackerelSpawnData(6f, LEFT_SPAWN_X, RIGHT_SPAWN_X, 1, OrdinaryFish::parabolicMotion));
        wave1.add(new MarlinSpawnData(8f, RIGHT_SPAWN_X, Marlin::normalMovement));
        easyWaves.add(wave1);

        // Wave 2: Mixed challenge
        Wave wave2 = new Wave(10f);
        wave2.add(new CrabSpawnData(0f, LEFT_SPAWN_X, Crab::normalMovement));
        wave2.add(new MarlinSpawnData(3f, RIGHT_SPAWN_X, Marlin::normalMovement));
        wave2.add(new JellyfishSpawnData(5f, LEFT_SPAWN_X, 400, 1.2f, JellyFish::jellyFishLeftToRightMovement));
        wave2.add(new CrabSpawnData(7f, RIGHT_SPAWN_X, Crab::normalMovement));
        easyWaves.add(wave2);

        // Wave 3: High speed wave
        Wave wave3 = new Wave(8f);
        wave3.add(new MarlinSpawnData(1f, LEFT_SPAWN_X, Marlin::normalMovement));
        wave3.add(new MarlinSpawnData(3f, RIGHT_SPAWN_X, Marlin::normalMovement));
        wave3.add(new MarlinSpawnData(5f, LEFT_SPAWN_X, Marlin::normalMovement));
        easyWaves.add(wave3);

        waveData.put(0, easyWaves);
    }

    private static int getDifficultyForRound(int currentWave){
        return 0;
    }

    public static Wave getWave(int waveId){
        return activeWaveList.get(waveId);
    }
}
