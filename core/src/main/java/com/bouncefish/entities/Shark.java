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
    private int attackCooldownCounter = GameConstants.SHARK_ATK_COOLDOWN;//ready to attack when the counter is 0'
    protected static final float BASE_SPEED = 800;
    public Shark(float spawnTime, float spawnX, boolean movingLeft, Consumer<Creature> movementFunction) {
        creatureId = 5;
        xVelocity = movingLeft? -BASE_SPEED:BASE_SPEED;
        yVelocity = 0;
        width = 260;
        height = 260;
        movementSpeed = 800;

        this.xPosition = spawnX;
        this.yPosition = GameConstants.WATER_LEVEL;
        this.movementSpeedMultiplier = 1;
        this.movementFunction = movementFunction;
        this.spawnTime = spawnTime;
        this.movingLeft = movingLeft;

        setBounds();
    }

    @Override
    public void updateBounds(){
        if(readyToAttack){
            bounds.setX(getX() - GameConstants.SHARK_DECTECTION_WIDTH);
            bounds.setY(getY());
        } else if(attacking){
            bounds.setX(getX());
            bounds.setY(getY() + height);
        }else{
            bounds.setX(getX());
            bounds.setY(getY());
        }

    }
    public static void initAnime(){
        Texture texture1 = new Texture("creatures/shark/s1.png");
        Texture texture2 = new Texture("creatures/shark/s2.png");
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
    @Override
    public void notAttacking(){
        attacking = false;
    }
    public boolean isInCoolDown(){
        return coolDown;
    }
    @Override
    protected void setBounds(){
        if(readyToAttack){//the shark is looking for the bouncefish
            bounds = new Rectangle(
                (getX() - GameConstants.SHARK_DECTECTION_WIDTH), getY(),
                GameConstants.SHARK_DECTECTION_WIDTH*2 + width, GameConstants.SHARK_DECTECTION_HEIGHT);
        }else if(attacking){ //the shark is jumping up
            bounds = new Rectangle(getX(), getY() + height, width, 42);//the hit box, a width X 42 box above the shark
        }else{//the shark is in the water(and cooling down)
            bounds = new Rectangle(getX(), getY(),
                getWidth(), getHeight());
        }
    }

    @Override
    public void handleTimeStep(){
        stateTime += Gdx.graphics.getDeltaTime();
        applyMovement();
        updateBounds();
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
            if(xVelocity > 0){
                setXVelocity(10);
            }else{
                setXVelocity(-10);
            }

            float height = GameConstants.SHARK_JUMP_HEIGHT - getY(); //v^2 = 2gh; the shark needs to have 0 speed when reach the jump height
            float initialSpeed = (float) Math.sqrt(2f * GameConstants.GRAVITY * height);
            setYVelocity(initialSpeed);
            setMovementFunction(Shark::attackMovement);
    }

    public static void attackMovement(Creature creature) {
        float dt = Gdx.graphics.getDeltaTime();
        creature.yVelocity -= GameConstants.GRAVITY * dt;
        creature.yPosition += creature.yVelocity * dt;
        creature.xPosition += creature.xVelocity * dt;

        if (Math.abs(creature.yPosition - GameConstants.SHARK_JUMP_HEIGHT) < 5f && creature.yVelocity > 0) {
            creature.yVelocity = 0f;
            creature.yPosition = GameConstants.SHARK_JUMP_HEIGHT;
        }

        if (creature.yPosition <= GameConstants.WATER_LEVEL) {
            creature.yPosition = GameConstants.WATER_LEVEL;
            creature.yVelocity = 0f;

            ((Shark) creature).attacking = false;//cast only once in a while, so maybe it's fine?
            ((Shark) creature).coolDown = true;
            ((Shark) creature).attackCooldownCounter = GameConstants.SHARK_ATK_COOLDOWN;

            creature.setBounds();
            if(!creature.movingLeft){
                creature.setXVelocity(Shark.BASE_SPEED);
            }else{
                creature.setXVelocity(-Shark.BASE_SPEED);
            }
            creature.setMovementFunction(Shark::swim);

        }
    }

    public static void swim(Creature creature){
        creature.xPosition += creature.xVelocity * creature.movementSpeedMultiplier * Gdx.graphics.getDeltaTime();
        if(creature.movingLeft){  //split into two cases so that the shark can move into the scene
            if(creature.xPosition < 0){
                creature.xVelocity = -creature.xVelocity;
                creature.movingLeft = false;
            }
        }else{
            if(creature.xPosition > GameConstants.Game_Width - creature.width){
                creature.xVelocity = -creature.xVelocity;
                creature.movingLeft = true;
            }
        }
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
