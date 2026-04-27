package com.bouncefish.entities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;
import com.bouncefish.gameplay.GameState;
import com.bouncefish.gameplay.GameStateHandler;
import com.bouncefish.gameplay.ProgressTracker;
import com.bouncefish.gameplay.SoundManager;
import com.bouncefish.utils.GameConstants;

import java.util.ArrayList;

public class BounceFish extends Creature {
    private boolean isBouncing = false;
    private float paralyzedTime;
    private boolean isParalyzed = false;
    private float spawnStasisTimer = 0; // Timer that tracks how long to remain in the air when spawned
    private ArrayList<Creature> creatureList; //Fish now knows what other objects exist in the game
    private static Animation<TextureRegion> downAnimation;
    private static Animation<TextureRegion> upAnimation;
    private Runnable deathEvent;

    public BounceFish(ArrayList<Creature> creatureList, Runnable deathEvent) {
        this.creatureId = 0;
        this.xPosition = Gdx.graphics.getWidth()* 0.5f;
        this.yPosition = 800;
        this.width = 150;
        this.height = 150;
        this.boundOffsetX = getWidth() * 0.15f;
        this.boundOffsetY = getHeight() * 0.15f;

        this.xVelocity = 0;
        this.yVelocity = 0;
        this.creatureList = creatureList;

        this.deathEvent = deathEvent;

        setBounds(); //Fish has a physical size needed for collision
        initAnime();
    }

    public void respawn(){
        //TODO: replace with animation of fish jumping out of water?
        this.xPosition = Gdx.graphics.getWidth() * 0.5f;
        this.yPosition = 800;
        this.spawnStasisTimer = 0;
        this.xVelocity = 0;
        this.yVelocity = 0;
        this.isBouncedOn = false;
        this.isParalyzed = false;
        this.paralyzedTime = 0;
    }

    @Override
    protected void setBounds(){
        this.bounds = new Rectangle(getX() + this.boundOffsetY, getY() + this.boundOffsetY, getWidth() * 0.7f, getHeight() * 0.7f);
    }

    public void reverseVelocityForBounce(){
        float currentY = getY();
        reverseVelocityForBounce(currentY);
    }

    public void reverseVelocityForBounce(float currentY){
        float targetY = Gdx.graphics.getHeight();
        reverseVelocityForBounce(targetY, currentY);
    }

    public void reverseVelocityForBounce(float targetY, float currentY){
        float deltaY = targetY - height - currentY;
        this.yVelocity = (float) Math.sqrt(2f * GameConstants.GRAVITY * deltaY);
        this.isBouncing = true;

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                isBouncing = false;
            }
        }, 0.5f);
    }

    public void applyDeathVelocity(){
        reverseVelocityForBounce(getY() + 300, getY());
    }

    public void applyFriction() {
        xVelocity *= GameConstants.FRICTION;
    }

    public boolean isBouncing() {
        return isBouncing;
    }
    public boolean isDead() {
        return isBouncedOn;
    }

    @Override
    public void handleTimeStep() {
        System.out.println("Y Velocity: " + yVelocity);

        if (this.yPosition <= -300){
            return;
        }

        if (spawnStasisTimer < GameConstants.SPAWN_STASIS_SECONDS){
            spawnStasisTimer += Gdx.graphics.getDeltaTime();
            return;
        }

        // Force of gravity
        boolean wasGoingUp = yVelocity > 0;
        decrementYVelocity(GameConstants.GRAVITY);
        if (wasGoingUp && yVelocity <= 0){
            stateTime = 0;
        }

        // Update x and y position based on velocity
        updatePosition();
        updateBounds();

        stateTime += Gdx.graphics.getDeltaTime();

        if (isBouncedOn){
            return;
        }
        if(isParalyzed){
            paralyzedTime += Gdx.graphics.getDeltaTime();
            if(paralyzedTime > GameConstants.MAX_PARALYZED_TIME){
                isParalyzed = false;
                isBouncedOn = false;
                paralyzedTime = 0F;
            }
        }
        //If fish touches any creature, Fish gets placed on top of creature and bounces upward.
        //Like Mario jumping on Enemy
        for (Creature creature : creatureList) {
            if (this.bounds.overlaps(creature.bounds)){
                stateTime = 0;
                int creatureId = creature.getCreatureId();
                if(creatureId == 5){ // Shark!
                    if(creature.isReadyToAttack()){
                        creature.attack();
                    }else if(creature.isAttacking()){
                        die(this.getY());
                        break;
                    }else{
                        bounce(creature);
                        //TODO decrease shark's hp
                    }
                }
                else if (creatureId == 2){ // Jellyfish!
                    isParalyzed = true;
                    applyDeathVelocity();
                    break;
                }
                else if (creature.isBouncable() && this.yVelocity < 0) {
                    bounce(creature);
                    creature.handleBouncedOn();
                    break;
                }
            }
        }

        // Lose Game! Initiate game over
        if (!GameConstants.IS_IMMORTAL){
            if (getY() <= GameConstants.WATER_LEVEL) {
                die();
                return;
            }
        }
        // If you are immortal, Keep bouncing
        else {
            if (getY() <= GameConstants.WATER_LEVEL) {
                groundBounce();
                return;
            }
        }

        //TODO: Audit? Might not be necessary if we are just using flat velocity instead of acceleration

        // Bouncing off left wall
        if (getX() <= 0) { //Fish reached left Edge
            setX(0); // Don't let it go outside screen
            setXVelocity(Math.abs(getXVelocity()) * GameConstants.BOUNCE_DAMPING); // -5 -> +5 After Bounce Should go Right(Positive)
        }

        // Bouncing off right wall
        if (getX() >= Gdx.graphics.getWidth() - 250) {
            setX(Gdx.graphics.getWidth() - 250);
            setXVelocity(-Math.abs(getXVelocity()) * GameConstants.BOUNCE_DAMPING);
        }
    }

    private void die(float heightOfDeath) {
        isBouncedOn = true;
        setY(heightOfDeath);
        applyDeathVelocity();
        xVelocity *= -1.2;

        deathEvent.run();
    }

    private void die(){
        die(GameConstants.WATER_LEVEL);
    }

    // Only used in cheats
    private void groundBounce(){
        setY(GameConstants.WATER_LEVEL);
        reverseVelocityForBounce();
        SoundManager.playBounceSound();
    }

    private void bounce(Creature creature){
        setY(creature.getHeight() + creature.getY());
        updateBounds();
        reverseVelocityForBounce();
        ProgressTracker.increaseScore();

        //TODO: call the getBouncedOn method of creature
        SoundManager.playBounceSound();
    }

    @Override
    public void handleBouncedOn() {
        System.out.println("This should never happen");
    }

    public boolean isParalyzed(){
        return isParalyzed;
    }
    @Override
    public TextureRegion getAnimeFrame(){
//        if (this.isDead() || this.isParalyzed()){
//            return
//        }
        // going up
        if (yVelocity >= 0){
            return upAnimation.getKeyFrame(getKeyFrameForCurrentSpeed(), false);
        }
        else {
            return downAnimation.getKeyFrame(getKeyFrameForCurrentSpeed(), false);
        }
    }

    private int getKeyFrameForCurrentSpeed(){
        if (yVelocity < -1200){
            return 2;
        }
        else if (yVelocity < -500){
            return 1;
        }
        else if (yVelocity < 0){
            return 0;
        }
        else if (yVelocity < 300){
            return 3;
        }
        else if (yVelocity < 800){
            return 2;
        }
        else if (yVelocity < 1200){
            return 1;
        }
        else if (yVelocity < 3000){
            return 0;
        }
        else {
            System.out.println("ALEX: INVALID SPEED!");
            return 0;
        }
    }

    public static void initAnime(){
        Texture down1 = new Texture("player/blob/down1.PNG");
        Texture down2 = new Texture("player/blob/down2.PNG");
        Texture down3 = new Texture("player/blob/down3.PNG");
        Texture down4 = new Texture("player/blob/down4.PNG");
        TextureRegion[] downFrames = new TextureRegion[4];
        downFrames[0] = new TextureRegion(down1);
        downFrames[1] = new TextureRegion(down2);
        downFrames[2] = new TextureRegion(down3);
        downFrames[3] = new TextureRegion(down4);
        downAnimation = new Animation<>(1F,downFrames);

        Texture up1 = new Texture("player/blob/up1.PNG");
        Texture up2 = new Texture("player/blob/up2.PNG");
        Texture up3 = new Texture("player/blob/up3.PNG");
        TextureRegion[] upFrames = new TextureRegion[3];
        upFrames[0] = new TextureRegion(up1);
        upFrames[1] = new TextureRegion(up2);
        upFrames[2] = new TextureRegion(up3);
        upAnimation = new Animation<>(1F,upFrames);
    }

}
