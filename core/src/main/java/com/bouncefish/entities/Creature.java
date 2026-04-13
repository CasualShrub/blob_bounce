package com.bouncefish.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import java.util.function.Consumer;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.bouncefish.utils.GameConstants;

public abstract class Creature {

    protected int creatureId;
    protected float xPosition;
    protected float yPosition;
    protected int width;
    protected int height;
    protected float xVelocity;
    protected float yVelocity;
    protected float movementSpeed;
    protected float movementSpeedMultiplier = 1;
    protected Consumer<Creature> movementFunction;
    protected Rectangle bounds; // Every object now has hitbox
    protected boolean isBouncable = true;

    // Runtime flags
    protected boolean isDead = false;
    protected double spawnTime = 0;
    protected float rotationAngle;//used to implement the curvilinear motion of ordinary fish
    protected float stateTime;
    public float getX() {
        return xPosition;
    }

    public void setX(float x) {
        xPosition = x;
    }

    public float getY() {
        return yPosition;
    }

    public void setY(float y) {
        yPosition = y;
    }

    // (Sample reference template for methods with return values)
    /** @return The current horizontal velocity of the creature. */
    public float getXVelocity(){
        return xVelocity;
    }
    public float getYVelocity(){
        return yVelocity;
    }

    public void setXVelocity(float xVelocity) {
        this.xVelocity = xVelocity;
    }
    public void setYVelocity(float yVelocity) {
        this.yVelocity = yVelocity;
    }

    public void decrementYVelocity(double yVelocity) {
        this.yVelocity -= yVelocity * Gdx.graphics.getDeltaTime();
    }
    public void setMovementFunction(Consumer<Creature> movementFunction){
        this.movementFunction = movementFunction;
    }

    public void setMovementSpeedMultiplier(float multiplier) {
        this.movementSpeedMultiplier = multiplier;
    }

    // Used only by the CreatureSpawner to define time to spawn
    public void setSpawnTime(double spawnTime){
        this.spawnTime = spawnTime;
    }
    public void setSpeedMultiplier(float multiplier) {
        this.movementSpeedMultiplier = multiplier;
    }

    public double getSpawnTime(){
        return this.spawnTime;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }
    public int getCreatureId() {
        return creatureId;
    }

    // Game loop
    public void updatePosition(){
        xPosition += xVelocity * Gdx.graphics.getDeltaTime();
        yPosition += yVelocity * Gdx.graphics.getDeltaTime();
        //Now Movement is Frame-rate independent.
    }

    public void spawn(float x, float y) {
        xPosition = x;
        yPosition = y;
    }
    //Created collision box
    public void setBounds(){
        bounds = new Rectangle(getX(),getY(),
            getWidth(), getHeight());
    }

    //Keeps box synced with movement
    //Every entity is rectangle in space
    public void updateBounds(){
        bounds.setX(getX());
        bounds.setY(getY());
    }

    protected void applyMovement(){
        movementFunction.accept(this);
    }

    public abstract void handleBouncedOn();

    public void handleTimeStep(){
        stateTime += Gdx.graphics.getDeltaTime();
        applyMovement();
        updateBounds();
    }
    public boolean isBouncable(){
        return isBouncable;
    }
    public abstract TextureRegion getAnimeFrame();
    public  Matrix4 getTextureRotationMatrix(){//used to implement the curvilinear motion of ordinary fish
        Matrix4 matrix = new Matrix4();
        float cx = xPosition;
        float cy = yPosition;

        matrix.translate(cx, cy,0);
        matrix.rotate(0,0,1,rotationAngle);
        matrix.translate(-cx, -cy,0);
        return matrix;
    }
    public boolean isRotated(){
        return rotationAngle != 0F;
    }


    // Override these if you want your creature to attack!
    public void attack(){
        System.out.println("I'm not supposed to be attacking!");
    }

    public boolean isReadyToAttack(){
        System.out.println("I'm not supposed to be attacking!");
        return false;
    }

    public boolean isAttacking(){
        System.out.println("I'm not supposed to be attacking!");
        return false;
    }
    //

}
