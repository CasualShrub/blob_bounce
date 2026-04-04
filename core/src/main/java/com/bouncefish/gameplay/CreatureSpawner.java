package com.bouncefish.gameplay;

import com.badlogic.gdx.Gdx;
import com.bouncefish.entities.Creature;
import com.bouncefish.utils.GameConstants;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

// This class is responsible for handling the spawn timing and types of creatures based on game time / points
public class CreatureSpawner {

    // This objects holds all the wave data in the game.
    private WaveRegistry waveRegistry;

    // This is the queue of creatures that will be spawned but haven't spawned yet
    private PriorityQueue<Creature> spawnQueue;

    // This is the list of creatures that have spawned and are currently being rendered
    private ArrayList<Creature> creatureList;
    private float spawnTimer;
    private Wave currentWave;
    private int currentWaveIndex;
    private float waveTimer; // Timer that resets to 0 when a new wave is spawned.
    private boolean isActive = false;

    public CreatureSpawner(ArrayList<Creature> creatureList) {
        this.creatureList = creatureList;
        this.waveRegistry = new WaveRegistry();
        this.spawnQueue = new PriorityQueue<Creature>(Comparator.comparingDouble(Creature::getSpawnTime));
        this.spawnTimer = 0;
        this.currentWaveIndex = 0;
        this.waveTimer = 0;

        //Populate creatureList with the first wave
        loadWave(this.currentWaveIndex);
    }

    public void setActive(boolean isActive){
        this.isActive = isActive;
    }

    public void handleTimeStep(){
        if (!this.isActive){
            return;
        }

        if (this.spawnQueue.isEmpty() && spawnTimer >= currentWave.getWaveDuration()){
            if (this.currentWaveIndex < GameConstants.MAXIMUM_HARDCODED_WAVES - 1){
                System.out.println("Alex: Spawning wave!");
                this.currentWaveIndex++;
                this.spawnTimer = 0;
                loadWave(this.currentWaveIndex);
            }
            return;
        }

        this.spawnTimer += Gdx.graphics.getDeltaTime();

        if (!this.spawnQueue.isEmpty() && this.spawnTimer >= this.spawnQueue.peek().getSpawnTime()){
            this.creatureList.add(this.spawnQueue.poll());
        }
    }

    public void cleanUpCreatures() {
        for (int i = this.creatureList.size() - 1; i > 0; i--) {
            Creature creature = this.creatureList.get(i);
            if (creature.getX() < -400 || creature.getX() > GameConstants.Game_Width){
                this.creatureList.remove(i);
            }
        }
    }

    private void loadWave(int waveIndex){
        this.currentWave = this.waveRegistry.getWave(waveIndex);
        this.spawnQueue.addAll(this.currentWave.getCreatures());
    }

    public ArrayList<Creature> spawnWave(int waveId) {
        return null;
    }


}
