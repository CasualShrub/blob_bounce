package com.bouncefish.gameplay;

import com.badlogic.gdx.Gdx;
import com.bouncefish.entities.Creature;
import com.bouncefish.entities.spawndata.CreatureSpawnData;
import com.bouncefish.utils.GameConstants;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

// This class is responsible for handling queueing waves and creatures up to be spawned into the game
public class CreatureSpawner {

    // This is the queue of creatures that will be spawned but haven't spawned yet
    private PriorityQueue<Creature> spawnQueue;

    // This is the list of creatures that have spawned and are currently being rendered
    private ArrayList<Creature> creatureList;
    private float spawnTimer;
    private Wave currentWave;
    private int currentWaveIndex;
    private boolean isActive = false;

    public CreatureSpawner(ArrayList<Creature> creatureList) {
        this.creatureList = creatureList;
        this.spawnQueue = new PriorityQueue<Creature>(Comparator.comparingDouble(Creature::getSpawnTime));
        this.spawnTimer = 0;
        this.currentWaveIndex = 0;
    }

    public void clearSpawnQueue(){
        this.spawnQueue.clear();
        this.currentWaveIndex = 0;
        this.spawnTimer = 0;
        this.currentWave = null;
        WaveRegistry.resetWaveRegistry();
    }

    public void setActive(boolean isActive){
        clearSpawnQueue();
        if (isActive){
            //Populate creatureList with the first wave
            WaveRegistry.generateWaves();
            WaveRegistry.prepareInitialWaves();
            loadWave(this.currentWaveIndex);
        }

        this.isActive = isActive;
    }

    // Every render frame, query the queue to see if a creature needs to be spawned
    public void handleTimeStep(){
        if (!this.isActive){
            return;
        }

        // If the queue is empty and the wave is over, load the next wave
        if (this.spawnQueue.isEmpty() && spawnTimer >= currentWave.getWaveDuration()){
            if (this.currentWaveIndex < GameConstants.MAXIMUM_HARDCODED_WAVES - 1){
                this.currentWaveIndex++;
                this.spawnTimer = 0;
                loadWave(this.currentWaveIndex);
            }
            return;
        }

        this.spawnTimer += Gdx.graphics.getDeltaTime();

        // Spawn any creatures whose spawn times have passed
        if (!this.spawnQueue.isEmpty() && this.spawnTimer >= this.spawnQueue.peek().getSpawnTime()){
            this.creatureList.add(this.spawnQueue.poll());
        }
    }

    // Clear any creatures that have left the screen
    public void cleanUpCreatures() {
        for (int i = this.creatureList.size() - 1; i > 0; i--) {
            Creature creature = this.creatureList.get(i);
            if (creature.getX() < -400 || creature.getX() > GameConstants.Game_Width){
                this.creatureList.remove(i);
            }
        }
    }

    // Instantiate all the creatures in the given wave and add them to the spawning queue
    private void loadWave(int waveIndex){
        this.currentWave = WaveRegistry.getWave(waveIndex);
        for (CreatureSpawnData<?> spawnData : this.currentWave.getCreaturesToSpawn()) {
            this.spawnQueue.add(spawnData.createInstance());
        }
    }
}
