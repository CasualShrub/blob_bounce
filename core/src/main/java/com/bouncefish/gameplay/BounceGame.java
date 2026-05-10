package com.bouncefish.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.bouncefish.entities.BounceFish;
import com.bouncefish.entities.*;
import com.bouncefish.leaderboard.LeaderboardService;
import com.bouncefish.utils.GameConstants;

import javax.swing.Timer;
import java.util.ArrayList;

public class BounceGame implements GestureDetector.GestureListener {

    private BounceFish _bounceFish;
    Timer _timer;
    private boolean _leftPressed;
    private boolean _rightPressed;
    private boolean _isActive;
    private CreatureSpawner _spawner;
    private ArrayList<Creature> _creatureList;
    private float cleanUpTimer = 0;
    private static int debugCounter = 0;

    private LeaderboardService leaderboardService;

    public BounceGame(LeaderboardService service) {
        _creatureList = new ArrayList<>();
        _bounceFish = new BounceFish(_creatureList, this::onPlayerDeath);
        _spawner = new CreatureSpawner(_creatureList);

        this.leaderboardService = service;
    }

    public void startGame(){
        _isActive = true;
        _spawner.setActive(true);
        _bounceFish.respawn();
    }

    // This function is called every frame
    public void timeStep() {
        if (!_isActive) {
            return;
        }

        // Logic updates regardless of whether the player is alive or not
        // However, we only take input if the game is in PLAYING state
        if (GameStateHandler.getCurrentState() == GameState.PLAYING) {
            if (!Gdx.input.isTouched() || _bounceFish.isParalyzed()){
                _leftPressed = false;
                _rightPressed = false;
                _bounceFish.setXVelocity(0);
            }

            // Handle user input
            if (_leftPressed) {
                _bounceFish.setXVelocity(-GameConstants.H_SPEED);
            }
            else if (_rightPressed) {
                _bounceFish.setXVelocity(GameConstants.H_SPEED);
            }
        } else {
            // Stop player movement during Game Over
            _bounceFish.setXVelocity(0);
        }

        // Spawner update loop.
        _spawner.handleTimeStep();

        // Bouncefish now receives here all crabs
        _bounceFish.handleTimeStep();

        // Inform all creatures to move!
        for (Creature creature: _creatureList) {
            if(creature.isActive()){
                creature.handleTimeStep();
            }
        }

        Water.handleTimeStep();

        // For optimization reasons, let's only try to loop through and deallocate creatures every X seconds
        this.cleanUpTimer += Gdx.graphics.getDeltaTime();
        if (cleanUpTimer >= GameConstants.CLEANUP_FREQUENCY){
            cleanUpTimer = 0;
            _spawner.cleanUpCreatures();
        }
    }

    public BounceFish getBounceFish() {
        return _bounceFish;
    }

    public ArrayList<Creature> getCreatureList() {
        return _creatureList;
    }


    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        if (GameStateHandler.getCurrentState() != GameState.PLAYING) return false;

        if (x > Gdx.graphics.getWidth() - GameConstants.RIGHT_CONTROL_BORDER) {
            _leftPressed = false;
            _rightPressed = true;
            return true;
        }
        else if (x < GameConstants.LEFT_CONTROL_BORDER){
            _leftPressed = true;
            _rightPressed = false;
            return true;
        }
        return false;
    }

    // Game Event Handlers
    public void onPlayerDeath(){
        // We no longer stop the spawner here so background keeps moving
        GameStateHandler.setCurrentState(GameState.GAME_OVER);
        Preferences prefs = Gdx.app.getPreferences(GameConstants.PREFS_NAME); // TODO: maybe make a helper function for this?
        String name = prefs.getString(GameConstants.PREF_PLAYER_NAME, "");
        this.leaderboardService.submitScore(name, ProgressTracker.getScore());
    }

    public void onMainMenu(){
        this._creatureList.clear();
    }

    // END Game Event Handlers

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
