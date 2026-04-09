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
    public Shark(float spawnTime, float spawnX, Consumer<Creature> movementFunction) {
        creatureId = 5;
        xVelocity = 0;
        yVelocity = 0;
        width = 260;
        height = 260;
        movementSpeed = 10;

        this.xPosition = spawnX;
        this.yPosition = GameConstants.Screen_Height/2;
        this.movementSpeedMultiplier = 1;
        this.movementFunction = movementFunction;
        this.spawnTime = spawnTime;

        setBounds();
    }
    private void updateAttackBounds(){
        if(readyToAttack){
            bounds.setX((int)getX() - GameConstants.SHARK_DECTECTION_WIDTH);
            bounds.setY((int)getY() - GameConstants.SHARK_DECTECTION_HEIGHT);
        } else{
            bounds.setX((int)getX());
            bounds.setY((int)getY());
        }

    }
    public static void leftToRight(Creature creature){
        creature.xPosition += creature.movementSpeed * creature.movementSpeedMultiplier * Gdx.graphics.getDeltaTime();
    }

    public static void rightToLeft(Creature creature){
        creature.xPosition -= creature.movementSpeed * creature.movementSpeedMultiplier * Gdx.graphics.getDeltaTime();
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
    public boolean isReadyToAttack(){
        return readyToAttack;
    }
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
            bounds = new Rectangle((int)getX() + width, (int)getY() + height, height, width);
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
    public void attack(){
            attacking = true;
            readyToAttack = false;
            setBounds(); //the shark is jumping up
            setXVelocity(0);
            setYVelocity(10);
    }
    public void attackingMovement(){
        yPosition += yVelocity * Gdx.graphics.getDeltaTime();
        if(yVelocity > 0){
            if(yPosition >= GameConstants.SHARK_JUMP_HEIGHT){
                yVelocity = -yVelocity;//make the transition smooth?
            }
        }else {
            if(yPosition <= GameConstants.WATER_LEVEL){
                setY(GameConstants.WATER_LEVEL);
                attacking = false; //the attack ends when the shark is back in water
                coolDown = true;
                attackCooldownCounter = GameConstants.SHARK_ATK_COOLDOWN;
                setBounds();//the shark is in the water(and cooling down)
            }
        }
    }

    @Override
    public void handleBouncedOn() {

    }

    @Override
    public TextureRegion getAnimeFrame() {
        return null;
    }
}
