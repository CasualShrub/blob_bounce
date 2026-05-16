package com.bouncefish.gameplay;

import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// This class holds all the data of enemies that will spawn in the game
public class WaveRegistry {
    private static final float LEFT_SPAWN_X = -200;
    private static float RIGHT_SPAWN_X = GameConstants.Game_Width;

    // Key: Round to spawn waves. 0 based
    // Value: Wave Data
    private static Map<Integer, Wave> activeWaveList = new HashMap<>();

    // Key: Wave Difficulty (Scales with points/time). 0 based
    // Values: ArrayList of wave data that the spawner can randomly choose from
    private static Map<Integer, ArrayList<Wave>> waveData = new HashMap<>();

    // This is where we define all wave data
    public static void prepareInitialWaves(){
        // Generate all waves??
        // TODO: consider batching them
        for (int i = 0; i < GameConstants.MAXIMUM_HARDCODED_WAVES; i++){
            activeWaveList.put(i, getRandomWaveForRound(i));
        }
    }

    // It is important to know the round before choosing from the pool
    private static Wave getRandomWaveForRound(int currentRound){
        int difficulty = getDifficultyForRound(currentRound);
        ArrayList<Wave> wavePool = waveData.get(difficulty); // Get the current difficulty waves
        int waveIndex = MathUtils.random((wavePool.size() - 1)); // -1 Because GDX random is right-inclusive
        return wavePool.remove(waveIndex);
    }

    public static void resetWaveRegistry(){
        activeWaveList.clear();
        waveData.clear();
    }

//    public static void generateWaves(){
//        ArrayList<Wave> easyWaves = new ArrayList<>();
//
//        // Java and LibGDX are code-centric, so we can't quite make visual ScriptableObjects like in Unity
//        // Which means we manually define all wave data here!
//
//        Wave wave1 = new Wave(9f);
//        wave1.add(new OceanSunfishSpawnData(0f,RIGHT_SPAWN_X,GameConstants.Screen_Height/2,1 ,OceanSunfish::normalMovement));
//        wave1.add(new OceanSunfishSpawnData(0f,LEFT_SPAWN_X,GameConstants.Screen_Height/2,1 ,OceanSunfish::normalMovement));
//        wave1.add(new MackerelSpawnData(0f,GameConstants.Game_Width - 50F,GameConstants.Game_Width/2 -100, 1, OrdinaryFish::parabolicMotion));
//        wave1.add(new MackerelSpawnData(0f,50F,GameConstants.Game_Width/2 + 100, 1, OrdinaryFish::parabolicMotion));
//        wave1.add(new CrabSpawnData(1f, LEFT_SPAWN_X, Crab::normalMovement));
//        wave1.add(new CrabSpawnData(2.5f, LEFT_SPAWN_X, Crab::normalMovement));
//        wave1.add(new CrabSpawnData(4f, LEFT_SPAWN_X, Crab::normalMovement));
//        wave1.add(new CrabSpawnData(5.8f, LEFT_SPAWN_X, Crab::normalMovement));
//        wave1.add(new CrabSpawnData(7f, RIGHT_SPAWN_X, Crab::normalMovement));
//        wave1.add(new CrabSpawnData(8f, LEFT_SPAWN_X, Crab::normalMovement));
//        wave1.add(new CrabSpawnData(8.2f, RIGHT_SPAWN_X, Crab::normalMovement));
//        wave1.add(new CrabSpawnData(9f, RIGHT_SPAWN_X, Crab::normalMovement));
//        wave1.add(new SharkSpawnData(1F,RIGHT_SPAWN_X,true, 1, Shark::swim));
//        wave1.add(new JellyfishSpawnData(1f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));
//        wave1.add(new JellyfishSpawnData(1.5f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));//for testing
//        wave1.add(new JellyfishSpawnData(2f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));//for testing
//        wave1.add(new JellyfishSpawnData(2.5f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));//for testing
//        wave1.add(new CrabSpawnData(12f, RIGHT_SPAWN_X, 0, 1.5f, Crab::normalMovement));
//        wave1.add(new MarlinSpawnData(1f, LEFT_SPAWN_X, Marlin::normalMovement));
//        easyWaves.add(wave1);
//
//        Wave wave2 = new Wave(6f);
//        wave2.add(new CrabSpawnData(0f, LEFT_SPAWN_X, 0, 1.5f, Crab::normalMovement));
//        wave2.add(new CrabSpawnData(1.3f, RIGHT_SPAWN_X, 0, 1.5f, Crab::normalMovement));
//        wave2.add(new CrabSpawnData(3f, LEFT_SPAWN_X, 0, 1.5f, Crab::normalMovement));
//        wave2.add(new CrabSpawnData(3.3f, RIGHT_SPAWN_X, 0, 1.7f, Crab::normalMovement));
//        wave2.add(new CrabSpawnData(5.7f, LEFT_SPAWN_X, 0, 1.5f, Crab::normalMovement));
//        wave2.add(new CrabSpawnData(5f, RIGHT_SPAWN_X, 0, 1.3f, Crab::normalMovement));
//        wave2.add(new CrabSpawnData(7f, LEFT_SPAWN_X, 0, 1.2f, Crab::normalMovement));
//        wave2.add(new CrabSpawnData(6f, RIGHT_SPAWN_X, 0, 1.5f, Crab::normalMovement));
//        wave2.add(new JellyfishSpawnData(1f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));
//        wave2.add(new JellyfishSpawnData(1.5f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));//for testing
//        wave2.add(new JellyfishSpawnData(2f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));//for testing
//        wave2.add(new JellyfishSpawnData(2.5f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));//for testing
//        easyWaves.add(wave2);
//
//        Wave wave3 = new Wave(6f);
//        wave3.add(new MackerelSpawnData(0f,LEFT_SPAWN_X, RIGHT_SPAWN_X, 1, OrdinaryFish::parabolicMotion));
//        wave3.add(new MackerelSpawnData(1f,LEFT_SPAWN_X, RIGHT_SPAWN_X, 1, OrdinaryFish::parabolicMotion));
//        wave3.add(new MackerelSpawnData(2f,LEFT_SPAWN_X, RIGHT_SPAWN_X, 1, OrdinaryFish::parabolicMotion));
//        wave3.add(new MackerelSpawnData(3f,LEFT_SPAWN_X, RIGHT_SPAWN_X, 1, OrdinaryFish::parabolicMotion));
//        wave3.add(new MackerelSpawnData(4f,LEFT_SPAWN_X, RIGHT_SPAWN_X, 1, OrdinaryFish::parabolicMotion));
//        wave3.add(new MackerelSpawnData(5f,LEFT_SPAWN_X, RIGHT_SPAWN_X, 1, OrdinaryFish::parabolicMotion));
//        wave3.add(new JellyfishSpawnData(1f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));
//        wave3.add(new JellyfishSpawnData(1.5f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));//for testing
//        wave3.add(new JellyfishSpawnData(2f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));//for testing
//        wave3.add(new JellyfishSpawnData(2.5f, LEFT_SPAWN_X, 300, 1.2f, JellyFish::jellyFishLeftToRightMovement));//for testing
//        wave3.add(new CrabSpawnData(6f, LEFT_SPAWN_X, 0, 1.2f, Crab::normalMovement));
//        easyWaves.add(wave3);
//
//        // Crab parade: crabs march in from alternating sides at a steady 1.5s cadence
//        Wave wave4 = new Wave(8.5f);
//        wave4.add(new CrabSpawnData(0f, LEFT_SPAWN_X, Crab::normalMovement));
//        wave4.add(new CrabSpawnData(2.5f, RIGHT_SPAWN_X, Crab::normalMovement));
//        wave4.add(new CrabSpawnData(4f, LEFT_SPAWN_X, Crab::normalMovement));
//        wave4.add(new CrabSpawnData(5.5f, RIGHT_SPAWN_X, Crab::normalMovement));
//        wave4.add(new CrabSpawnData(7f, LEFT_SPAWN_X, Crab::normalMovement));
//        wave4.add(new CrabSpawnData(8.5f, RIGHT_SPAWN_X, Crab::normalMovement));
//        easyWaves.add(wave4);
//
//        Wave wave5 = new Wave(7f);
//        wave5.add(new MackerelSpawnData(0f, LEFT_SPAWN_X, RIGHT_SPAWN_X, 1f, OrdinaryFish::parabolicMotion));
//        wave5.add(new MackerelSpawnData(1.3f, RIGHT_SPAWN_X, LEFT_SPAWN_X, 1f, OrdinaryFish::parabolicMotion));
//        wave5.add(new MackerelSpawnData(3f, LEFT_SPAWN_X, RIGHT_SPAWN_X, 1f, OrdinaryFish::parabolicMotion));
//        wave5.add(new MackerelSpawnData(4.2f, RIGHT_SPAWN_X, LEFT_SPAWN_X, 1f, OrdinaryFish::parabolicMotion));
//        wave5.add(new MackerelSpawnData(5.3f, LEFT_SPAWN_X, RIGHT_SPAWN_X, 1.1f, OrdinaryFish::parabolicMotion));
//        wave5.add(new MackerelSpawnData(7f, RIGHT_SPAWN_X, LEFT_SPAWN_X, 1.1f, OrdinaryFish::parabolicMotion));
//        easyWaves.add(wave5);
//
//        Wave wave6 = new Wave(8.5f);
//        wave6.add(new CrabSpawnData(0f, LEFT_SPAWN_X, Crab::normalMovement));
//        wave6.add(new MackerelSpawnData(1.0f, RIGHT_SPAWN_X, LEFT_SPAWN_X, 1f, OrdinaryFish::parabolicMotion));
//        wave6.add(new CrabSpawnData(2f, RIGHT_SPAWN_X, Crab::normalMovement));
//        wave6.add(new MackerelSpawnData(3.5f, LEFT_SPAWN_X, RIGHT_SPAWN_X, 1f, OrdinaryFish::parabolicMotion));
//        wave6.add(new CrabSpawnData(5f, LEFT_SPAWN_X, Crab::normalMovement));
//        wave6.add(new MackerelSpawnData(6f, RIGHT_SPAWN_X, LEFT_SPAWN_X, 1f, OrdinaryFish::parabolicMotion));
//        wave6.add(new CrabSpawnData(8.5f, RIGHT_SPAWN_X, Crab::normalMovement));
//        easyWaves.add(wave6);
//
//        Wave wave7 = new Wave(6.2f);
//        wave7.add(new CrabSpawnData(0f, LEFT_SPAWN_X, Crab::normalMovement));
//        wave7.add(new CrabSpawnData(1f, RIGHT_SPAWN_X, Crab::normalMovement));
//        wave7.add(new CrabSpawnData(1.7f, LEFT_SPAWN_X, Crab::normalMovement));
//        wave7.add(new CrabSpawnData(2.4f, RIGHT_SPAWN_X, Crab::normalMovement));
//        wave7.add(new CrabSpawnData(2.6f, RIGHT_SPAWN_X, 0, 1.3f, Crab::normalMovement));
//        wave7.add(new CrabSpawnData(3.5f, LEFT_SPAWN_X, 0, 1.2f, Crab::normalMovement));
//        wave7.add(new CrabSpawnData(4.5f, RIGHT_SPAWN_X, 0, 1.2f, Crab::normalMovement));
//        wave7.add(new CrabSpawnData(6f, LEFT_SPAWN_X, 0, 1.4f, Crab::normalMovement));
//        wave7.add(new CrabSpawnData(6.2f, RIGHT_SPAWN_X, 0, 1.4f, Crab::normalMovement));
//        easyWaves.add(wave7);
//
//        Wave wave8 = new Wave(8.5f);
//        wave8.add(new MackerelSpawnData(0f, LEFT_SPAWN_X, RIGHT_SPAWN_X, 1f, OrdinaryFish::parabolicMotion));
//        wave8.add(new MackerelSpawnData(1.3f, RIGHT_SPAWN_X, LEFT_SPAWN_X, 1f, OrdinaryFish::parabolicMotion));
//        wave8.add(new MackerelSpawnData(2.7f, LEFT_SPAWN_X, RIGHT_SPAWN_X, 1f, OrdinaryFish::parabolicMotion));
//        wave8.add(new MackerelSpawnData(4f, RIGHT_SPAWN_X, LEFT_SPAWN_X, 1f, OrdinaryFish::parabolicMotion));
//        wave8.add(new CrabSpawnData(4f, LEFT_SPAWN_X, Crab::normalMovement));
//        wave8.add(new CrabSpawnData(5.5f, RIGHT_SPAWN_X, Crab::normalMovement));
//        wave8.add(new CrabSpawnData(6.5f, LEFT_SPAWN_X, 0, 1.2f, Crab::normalMovement));
//        wave8.add(new CrabSpawnData(8f, RIGHT_SPAWN_X, 0, 1.2f, Crab::normalMovement));
//        wave8.add(new CrabSpawnData(8.5f, LEFT_SPAWN_X, 0, 1.3f, Crab::normalMovement));
//        easyWaves.add(wave8);
//
//        waveData.put(0, easyWaves);
//    }

    public static void generateWaves() {

        ArrayList<Wave> easyWaves = new ArrayList<>();

        FileHandle file = Gdx.files.internal("waves_test.json");

//        if (!file.exists()) {
//            System.out.println("ERRRRORR: waves.json not found!");
//            return;
//        }

        Json json = new Json();
        JsonValue root = json.fromJson(null, file);

//        if (root == null) {
//            System.out.println("ERRRRORR: Failed to parse waves.json!");
//            return;
//        }

        JsonValue waves = root.get("waves");

        if (waves == null) {
            System.out.println("ERRRRORR: No waves array found!");
            return;
        }

        for (JsonValue waveJson : waves) {
            float duration = waveJson.getFloat("duration");
            Wave wave = new Wave(duration);
            JsonValue spawns = waveJson.get("spawns");

//            if (spawns == null) {
//                System.out.println("ERRRRORR: Wave missing spawns!");
//                continue;
//            }
            for (JsonValue spawn : spawns) {

                String type = spawn.getString("type");
                float time = spawn.getFloat("time");
                float speedMultiplier = spawn.getFloat("speedMultiplier", 1f);

                switch (type) {
                    case "Crab":
                        float crabX = resolveConstant(spawn.getString("x"));
                        float crabY = spawn.getFloat("yOffset", 0f);

                        wave.add(new CrabSpawnData(time, crabX, crabY, speedMultiplier, Crab::normalMovement));

                        break;

                    case "Mackerel":
                        float startX = resolveConstant(spawn.getString("startX"));
                        startX /= spawn.getFloat("startXDivide", 1f);
                        startX += spawn.getFloat("startXOffset", 0f);

                        float targetX = resolveConstant(spawn.getString("targetX"));
                        targetX /= spawn.getFloat("targetXDivide", 1f);
                        targetX += spawn.getFloat("targetXOffset", 0f);

                        wave.add(new MackerelSpawnData(time, startX, targetX, speedMultiplier, OrdinaryFish::parabolicMotion));
                        break;

                    case "OceanSunfish":
                        float sunfishX = resolveConstant(spawn.getString("x"));
                        float sunfishY = resolveConstant(spawn.getString("y"));

                        sunfishY /= spawn.getFloat("yDivide", 1f);
                        sunfishY += spawn.getFloat("yOffset", 0f);

                        wave.add(new OceanSunfishSpawnData(time, sunfishX, sunfishY, speedMultiplier, OceanSunfish::normalMovement));
                        break;

                    default:
                        System.out.println(
                            "ERRRRORR: Unknown spawn type: " + type
                        );
                        break;
                }
            }
            easyWaves.add(wave);
        }
        waveData.put(0, easyWaves);
        GameConstants.MAXIMUM_HARDCODED_WAVES = waveData.size();
    }

    private static float resolveConstant(String value) {

        switch (value) {
            case "LEFT_SPAWN_X":
                return LEFT_SPAWN_X;
            case "RIGHT_SPAWN_X":
                return RIGHT_SPAWN_X;
            case "GAME_WIDTH":
                return GameConstants.Game_Width;
            case "SCREEN_HEIGHT":
                return GameConstants.Screen_Height;
            default:
                return Float.parseFloat(value);
        }
    }

    // Converts current round to difficulty
    // TODO: Consider moving to a static helper class?
    private static int getDifficultyForRound(int currentWave){
        // If current wave is 5 or less, then it is an easy wave.
//        if (currentWave <= 5) {
//            return 0;
//        }
        if (currentWave <= 10){
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
