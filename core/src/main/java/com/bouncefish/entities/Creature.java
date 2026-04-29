package com.bouncefish.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import java.util.function.Consumer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Matrix4;
import com.bouncefish.utils.GameConstants;

public abstract class Creature {

    // Creature Type Unique Identifier
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
    protected boolean isBouncedOn = false; //bouncefish doesn't kill
    protected double spawnTime = 0;
    protected float rotationAngle;//used to implement the curvilinear motion of ordinary fish
    protected float stateTime;
    protected float boundScaleX = 1F;
    protected float boundScaleY = 1F;
    protected float boundOffsetX = 0F;
    protected float boundOffsetY = 0F;
    protected boolean movingLeft;
    protected boolean active = true;
    protected boolean inWater;//added this because it seems that the creature never deallocates,
    // and keeps calling Water.playSplash() after falling into the water. haven't got time to figure
    // this out, though; just a simple fix, so no data protection mechanisms yet
    protected boolean jumpedOutOfWater; //another hack, specifically for ordinary fish parabolic motion. Ordinary fish is already in the water at first(y<water level)
    public boolean isMovingLeft(){
        return movingLeft;
    }

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
    public Rectangle getBounds(){
        return this.bounds;
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

    public void setBoundScale(float xScale, float yScale){
        boundScaleX = xScale;
        boundScaleY = yScale;
        boundOffsetX = (1 - boundOffsetX) * width / 2;
        boundOffsetY = (1 - boundOffsetY) * height / 2;
    }
    //Created collision box
    protected void setBounds(){
        //seems to work well without recentering
        bounds = new Rectangle(getX(), getY(),
            getWidth()*boundScaleX, getHeight()*boundScaleY);
    }

    // Update hitbox position
    public void updateBounds(){
        //seems to work well without recentering
        bounds.setX(getX() + boundOffsetX);
        bounds.setY(getY() + boundOffsetY);
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
    public void notAttacking(){
        System.out.println("I'm not supposed to be attacking!");
    }
    public static boolean movingRight(Creature creature){
        return creature.xVelocity > 0;
    }
    public float getLeadingCoefficient(){
        System.out.println("No leading coefficient for this creature");
        return 0;
    }
    public float getLinearCoefficient(){
        System.out.println("No linear coefficient for this creature");
        return 0;
    }
    public float getStartX(){
        System.out.println("No start x for this creature");
        return GameConstants.Game_Width/2;
    }

    // Use this if you need to infer whether or not the creature should move left based purely off of spawnX
    protected boolean shouldMoveLeft(float currentPosition) {
        return currentPosition >= GameConstants.Game_HalfWidth;
    }

    public float getParabolicMovementMaxHeight(){
        System.out.println("No parabola for this creature");
        return 0;
    }
    public float getParabolicMovementLeftEnd(){
        System.out.println("No parabola for this creature");
        return 0;
    }
    public float getParabolicMovementRightEnd(){
        System.out.println("No parabola for this creature");
        return 0;
    }
    public boolean isActive(){
        return active;
    }
    public void deactivate(){
        active = false;
    }
}
