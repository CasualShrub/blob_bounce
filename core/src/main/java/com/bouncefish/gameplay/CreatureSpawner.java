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
    private WaveRegistry _waveRegistry;

    // This is the queue of creatures that will be spawned but haven't spawned yet
    private PriorityQueue<Creature> _spawnQueue;

    // This is the list of creatures that have spawned and are currently being rendered
    private ArrayList<Creature> _creatureList;
    private float _timer;
    private int _currentWave;

    private boolean _isActive = false;
    private float _waveDelay;

    public CreatureSpawner(ArrayList<Creature> creatureList) {
        _creatureList = creatureList;
        _waveRegistry = new WaveRegistry();
        _spawnQueue = new PriorityQueue<Creature>(Comparator.comparingDouble(Creature::getSpawnTime));
        _timer = 0;
        _currentWave = 1;

        //Populate creatureList with the first wave
        loadWave(_currentWave);
    }

    public void setActive(boolean isActive){
        _isActive = isActive;
    }

    public void handleTimeStep(){
        if (!_isActive){
            return;
        }

        if (_spawnQueue.isEmpty()){
            _currentWave++;
            if (_currentWave <= GameConstants.MAXIMUM_HARDCODED_WAVES){
                loadWave(_currentWave);
            }
            return;
        }

        _timer += Gdx.graphics.getDeltaTime();

        while (!_spawnQueue.isEmpty() && _timer >= _spawnQueue.peek().getSpawnTime()){
            _creatureList.add(_spawnQueue.poll());
        }
    }

    public void cleanUpCreatures() {
        for (Creature creature: _creatureList) {
            if (creature.getX() < -400 || creature.getX() > GameConstants.Game_Width){
                _creatureList.remove(creature);
            }
        }
    }

    private void loadWave(int waveToLoad){
        _spawnQueue.addAll(_waveRegistry.getWave(waveToLoad).getCreatures());
    }

    public ArrayList<Creature> spawnWave(int waveId) {
        return null;
    }


}
