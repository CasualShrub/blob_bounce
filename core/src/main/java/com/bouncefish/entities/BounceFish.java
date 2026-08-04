package com.bouncefish.entities;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;
import com.bouncefish.gameplay.GameState;
import com.bouncefish.gameplay.GameStateHandler;
import com.bouncefish.gameplay.PowerUpType;
import com.bouncefish.gameplay.ProgressTracker;
import com.bouncefish.gameplay.SoundManager;
import com.bouncefish.utils.GameConstants;

import java.util.ArrayList;

public class BounceFish extends Creature {
    private boolean isBouncing = false;
    private float paralyzedTime;
    private boolean isParalyzed = false;
    private float spawnStasisTimer = 0;
    private boolean hasPowerUp = false;
    private PowerUpType powerUpType = PowerUpType.NONE;
    private boolean isFloating = false;
    private float floatTimer = 0;
    private float powerUpCooldown = 0f;
    private float deathTime = 0f;
    private static final float GHOST_RISE_SPEED = 120f;
    private static final float DEATH_FRAME_DURATION = 0.18f;
    private ArrayList<Creature> creatureList; //Fish now knows what other objects exist in the game
    private static Animation<TextureRegion> downAnimation;
    private static Animation<TextureRegion> upAnimation;
    private static Animation<TextureRegion> downAnimation_paralyzed;
    private static Animation<TextureRegion> upAnimation_paralyzed;
    private static Animation<TextureRegion> deathAnimation;
    private Runnable deathEvent;

    public BounceFish(ArrayList<Creature> creatureList, Runnable deathEvent) {
        this.creatureId = 0;
        this.yPosition = 800;
        this.width = 190;
        this.height = 190;
        this.xPosition = Gdx.graphics.getWidth() * 0.5f - (this.getWidth() * 0.5f);
        this.boundOffsetX = getWidth() * 0.225f;
        this.boundOffsetY = getHeight() * 0.225f;

        this.xVelocity = 0;
        this.yVelocity = 0;
        this.creatureList = creatureList;

        this.deathEvent = deathEvent;

        setBounds(); //Fish has a physical size needed for collision
        initAnime();
    }

    public void respawn(){
        //TODO: replace with animation of fish jumping out of water?
        this.xPosition = Gdx.graphics.getWidth() * 0.5f - (this.getWidth() * 0.5f);
        this.yPosition = 800;
        this.spawnStasisTimer = 0;
        this.xVelocity = 0;
        this.yVelocity = 0;
        this.isBouncedOn = false;
        this.isParalyzed = false;
        this.paralyzedTime = 0;
        this.hasPowerUp = false;
        this.powerUpType = PowerUpType.NONE;
        this.isFloating = false;
        this.floatTimer = 0;
        this.powerUpCooldown = 0f;
        this.deathTime = 0f;
    }

    @Override
    protected void setBounds(){
        this.bounds = new Rectangle(getX() + this.boundOffsetX, getY() + this.boundOffsetY, getWidth() * 0.55f, getHeight() * 0.55f);
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

        // Dead — play hurt-to-ghost animation while rising slowly upward
        if (isBouncedOn) {
            deathTime += Gdx.graphics.getDeltaTime();
            yVelocity = GHOST_RISE_SPEED;
            xVelocity = 0;
            updatePosition();
            updateBounds();
            return;
        }

        // Tick down cooldown
        if (powerUpCooldown > 0) {
            powerUpCooldown -= Gdx.graphics.getDeltaTime();
            if (powerUpCooldown < 0) powerUpCooldown = 0;
        }

        // Force of gravity - skipped while power-up float is active
        if (isFloating) {
            floatTimer += Gdx.graphics.getDeltaTime();
            yVelocity = 0;
            if (floatTimer >= GameConstants.FLOAT_DURATION) {
                isFloating = false;
                floatTimer = 0;
            }
        } else {
            boolean wasGoingUp = yVelocity > 0;
            decrementYVelocity(GameConstants.GRAVITY);
            if (wasGoingUp && yVelocity <= 0) {
                stateTime = 0;
            }
        }

        // Update x and y position based on velocity
        updatePosition();
        updateBounds();

        stateTime += Gdx.graphics.getDeltaTime();

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
                        //die(this.getY());
                        break;
                    }else{
                        bounce(creature);
                        creature.takeDamage();
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
                    if (powerUpCooldown <= 0) {
                        if (creature.getCreatureId() == 1) { // Crab = ground pound power up!
                            hasPowerUp = true;
                            powerUpType = PowerUpType.GROUND_POUND;
                        } else if (creature.getCreatureId() == 4) { // Mackerel = float power up!
                            hasPowerUp = true;
                            powerUpType = PowerUpType.FLOAT;
                        }
                    }
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
        xVelocity = 0;
        yVelocity = GHOST_RISE_SPEED;
        deathTime = 0f;

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
        setY(creature.getBounds().getY() + creature.getBounds().getHeight());
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

    public void activatePowerUp() {
        if (!hasPowerUp) return;
        hasPowerUp = false;
        powerUpCooldown = GameConstants.POWER_UP_COOLDOWN_DURATION;
        if (powerUpType == PowerUpType.FLOAT) {
            isFloating = true;
            floatTimer = 0;
            yVelocity = 0;
        } else if (powerUpType == PowerUpType.GROUND_POUND) {
            isFloating = false;
            yVelocity = GameConstants.GROUND_POUND_VELOCITY;
        }
        powerUpType = PowerUpType.NONE;
    }

    public boolean hasPowerUp() {
        return hasPowerUp;
    }

    public PowerUpType getPowerUpType() {
        return powerUpType;
    }

    public String getPowerUpLabel() {
        switch (powerUpType) {
            case FLOAT:
                return "FLOATING FISH";
            case GROUND_POUND:
                return "CRABSH DOWN";
            default:
                return "";
        }
    }

    public boolean isFloating() {
        return isFloating;
    }

    // Returns 1 when float just started, 0 when it has expired
    public float getFloatProgress() {
        return 1f - (floatTimer / GameConstants.FLOAT_DURATION);
    }

    public boolean isOnCooldown() {
        return powerUpCooldown > 0;
    }
    public float getCooldownProgress() {
        return powerUpCooldown / GameConstants.POWER_UP_COOLDOWN_DURATION;
    }
    public float getPowerUpCooldownRemaining() {
        return powerUpCooldown;
    }

    public boolean isInStasis() {
        return spawnStasisTimer < GameConstants.SPAWN_STASIS_SECONDS;
    }
    public float getStasisTimeRemaining() {
        return Math.max(0f, GameConstants.SPAWN_STASIS_SECONDS - spawnStasisTimer);
    }

    public boolean isParalyzed(){
        return isParalyzed;
    }
    @Override
    public TextureRegion getAnimeFrame(){
        if (this.isDead()){
            return deathAnimation.getKeyFrame(deathTime, false);
        }
        // going up
        if (yVelocity >= 0){
            if(isParalyzed){
                return upAnimation_paralyzed.getKeyFrame(getKeyFrameForCurrentSpeed(), false);
            }
            return upAnimation.getKeyFrame(getKeyFrameForCurrentSpeed(), false);
        }
        else {
            if(isParalyzed){
                return downAnimation_paralyzed.getKeyFrame(getKeyFrameForCurrentSpeed(), false);
            }
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
        TextureRegion[] downFrames = new TextureRegion[4];
        downFrames[0] = new TextureRegion(down1);
        downFrames[1] = new TextureRegion(down2);
        downFrames[2] = new TextureRegion(down3);
        downAnimation = new Animation<>(1F,downFrames);

        Texture up1 = new Texture("player/blob/up1.PNG");
        Texture up2 = new Texture("player/blob/up2.PNG");
        Texture up3 = new Texture("player/blob/up3.PNG");
        TextureRegion[] upFrames = new TextureRegion[3];
        upFrames[0] = new TextureRegion(up1);
        upFrames[1] = new TextureRegion(up2);
        upFrames[2] = new TextureRegion(up3);
        upAnimation = new Animation<>(1F,upFrames);

        down1 = new Texture("player/blob/down1_.PNG");
        down2 = new Texture("player/blob/down2_.PNG");
        down3 = new Texture("player/blob/down3_.PNG");
        downFrames = new TextureRegion[4];
        downFrames[0] = new TextureRegion(down1);
        downFrames[1] = new TextureRegion(down2);
        downFrames[2] = new TextureRegion(down3);
        downAnimation_paralyzed = new Animation<>(1F,downFrames);

        up1 = new Texture("player/blob/up1_.PNG");
        up2 = new Texture("player/blob/up2_.PNG");
        up3 = new Texture("player/blob/up3_.PNG");
        upFrames = new TextureRegion[3];
        upFrames[0] = new TextureRegion(up1);
        upFrames[1] = new TextureRegion(up2);
        upFrames[2] = new TextureRegion(up3);
        upAnimation_paralyzed = new Animation<>(1F,upFrames);

        Texture hurt1  = new Texture("player/blob/blobhurt1.png");
        Texture hurt2  = new Texture("player/blob/blobhurt2.png");
        Texture ghost1 = new Texture("player/blob/blobghost1.png");
        Texture ghost2 = new Texture("player/blob/blobghost2.png");
        Texture ghost3 = new Texture("player/blob/blobghost3.png");
        Texture ghost4 = new Texture("player/blob/blobghost4.png");
        TextureRegion[] deathFrames = new TextureRegion[6];
        deathFrames[0] = new TextureRegion(hurt1);
        deathFrames[1] = new TextureRegion(hurt2);
        deathFrames[2] = new TextureRegion(ghost1);
        deathFrames[3] = new TextureRegion(ghost2);
        deathFrames[4] = new TextureRegion(ghost3);
        deathFrames[5] = new TextureRegion(ghost4);
        deathAnimation = new Animation<>(DEATH_FRAME_DURATION, deathFrames);
    }

}
