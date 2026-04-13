package com.bouncefish.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.bouncefish.utils.GameConstants;

import com.badlogic.gdx.math.Rectangle;
import java.util.function.Consumer;

public class Shark extends Creature{
    private static Animation<TextureRegion> sharkAnimationSwim;
    private static Animation<TextureRegion> sharkAnimationAttack;
    //private Rectangle attackBounds;
    private boolean readyToAttack = true;
    private boolean attacking;
    private boolean coolDown; //needs to cooldown after attacking
    private int attackCooldownCounter = GameConstants.SHARK_ATK_COOLDOWN;//ready to attack when the counter is 0
    private boolean swimLeft; //TODO: choose a random dir instead?
    public Shark(float spawnTime, float spawnX, Consumer<Creature> movementFunction) {
        creatureId = 5;
        xVelocity = 0;
        yVelocity = 0;
        width = 260;
        height = 260;
        movementSpeed = 500;

        this.xPosition = spawnX;
        this.yPosition = GameConstants.WATER_LEVEL;
        this.movementSpeedMultiplier = 1;
        this.movementFunction = movementFunction;
        this.spawnTime = spawnTime;

        setBounds();
    }
    private void updateAttackBounds(){
        if(readyToAttack){
            bounds.setX((int)getX() - GameConstants.SHARK_DECTECTION_WIDTH);
            bounds.setY((int)getY() - GameConstants.SHARK_DECTECTION_HEIGHT);
        } else if(attacking){
            bounds.setX((int)getX());
            bounds.setY((int)getY() + height);
        }else{
            bounds.setX((int)getX());
            bounds.setY((int)getY());
        }

    }
    public static void initAnime(){
        Texture texture1 = new Texture("s1.png");
        Texture texture2 = new Texture("s2.png");
        TextureRegion[] frames1 = new TextureRegion[1];
        TextureRegion[] frames2 = new TextureRegion[1];
        frames1[0] = new TextureRegion(texture1);
        frames2[0] = new TextureRegion(texture2);
        sharkAnimationSwim = new Animation<>(0.5F,frames1);
        sharkAnimationAttack = new Animation<>(0.5F,frames2);
    }

    @Override
    public boolean isReadyToAttack(){
        return readyToAttack;
    }

    @Override
    public boolean isAttacking(){
        return attacking;
    }
    public boolean isInCoolDown(){
        return coolDown;
    }
    @Override
    public void setBounds(){

        if(readyToAttack){//the shark is looking for the bouncefish
            bounds = new Rectangle(
                (int)(getX() - GameConstants.SHARK_DECTECTION_WIDTH),
                (int)(getY() - GameConstants.SHARK_DECTECTION_HEIGHT),
                GameConstants.SHARK_DECTECTION_WIDTH*2, GameConstants.SHARK_DECTECTION_HEIGHT*2);
        }else if(attacking){ //the shark is jumping up
            bounds = new Rectangle((int)getX(), (int)getY() + height, 42, 42);//the hit box
        }else{//the shark is in the water(and cooling down)
            bounds = new Rectangle((int)getX(), (int)getY(),
                getWidth(), getHeight());
        }
    }
    @Override
    public void handleTimeStep(){
        stateTime += Gdx.graphics.getDeltaTime();
        applyMovement();
        updateBounds();
        updateAttackBounds();
        if(coolDown && --attackCooldownCounter <= 0){
            readyToAttack = true;
            coolDown = false;
            setBounds();//the shark is looking for the bouncefish
        }
    }

    @Override
    public void attack(){
            attacking = true;
            readyToAttack = false;
            setBounds(); //the shark is jumping up
            setXVelocity(0);
            setYVelocity(800);
            setMovementFunction(Shark::attackMovement);
    }
    public static void attackMovement(Creature creature){
        creature.yPosition += creature.yVelocity * Gdx.graphics.getDeltaTime();
        if(creature.yVelocity > 0){
            if(creature.yPosition >= GameConstants.SHARK_JUMP_HEIGHT){
                creature.yVelocity = -500;//TODO: make the transition smooth?
            }
        }else {
            if(creature.yPosition <= GameConstants.WATER_LEVEL){
                creature.setY(GameConstants.WATER_LEVEL);
                ((Shark) creature).attacking = false; //the attack ends when the shark is back in water
                ((Shark) creature).coolDown = true;
                ((Shark) creature).attackCooldownCounter = GameConstants.SHARK_ATK_COOLDOWN;
                creature.setBounds();//the shark is in the water(and cooling down)
                creature.setMovementFunction(Shark::leftToRight);
                creature.setMovementSpeedMultiplier(0);//this line is used for testing
            }
        }
    }
    public static void leftToRight(Creature creature){
//TODO: right now the shark appear from one side, attack the player, and then go away. Maybe the shark should turn around once reach the edge
        creature.xPosition += creature.movementSpeed * creature.movementSpeedMultiplier * Gdx.graphics.getDeltaTime();
    }

    public static void rightToLeft(Creature creature){
        creature.xPosition -= creature.movementSpeed * creature.movementSpeedMultiplier * Gdx.graphics.getDeltaTime();
    }
    @Override
    public void handleBouncedOn() {

    }

    @Override
    public TextureRegion getAnimeFrame() {
        if(readyToAttack || coolDown){
            return sharkAnimationSwim.getKeyFrame(stateTime,true);
        }else{
            return sharkAnimationAttack.getKeyFrame(stateTime,true);
        }
    }
}
