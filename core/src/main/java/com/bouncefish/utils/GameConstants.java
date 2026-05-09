package com.bouncefish.utils;

import com.badlogic.gdx.Gdx;

public final class GameConstants {

    private GameConstants() { } //We never want to instantiate this -- think like C# static class

    // If we want to make any of these editable at runtime, then we'll have to convert this class into something not 'final'
    public static final float GRAVITY = 3000;       // Persistent downward acceleration
    public static final float BOUNCE_DAMPING = 1; // Energy retained on bounce (maybe can make this creature specific?)
    public static final double DEATH_BOUNCE_DAMPING = 0.3; // Purely visual damping multiplier for when the player dies
    public static final double FRICTION = 0.98;      // Friction when bouncing on a creature (maybe can make this creature specific?)
    //public static final double H_ACCEL = 45;        // How fast bouncefish should speed up when inputting left/right (convert this to immediate velocity?)
    public static final float H_SPEED = 800; // Base value for horizontal 'movement' speed
    public static final double MAX_H_SPEED = 700;    // Maximum horizontal speed
    public static final float WATER_LEVEL = 50; // Y position of ground level
    public static final int RIGHT_CONTROL_BORDER = 400;
    public static final int LEFT_CONTROL_BORDER = 400;
    public static boolean IS_DEBUG = true;
    public static boolean IS_IMMORTAL = true; // Cheatcode: Use this if you want to be able to bounce on the ground just for testing purposes.

    public static final int MAXIMUM_HARDCODED_WAVES = 8; // Maximum value of waves to do before reaching the endless phase of the game.

    // These are just some wrappers because Gdx.graphics.getWidth feels too long
    public static float Game_Width = Gdx.graphics.getWidth();
    public static float Screen_Height = Gdx.graphics.getHeight();
    public static float Game_HalfWidth = Game_Width / 2;
    public static final float MAX_PARALYZED_TIME = 1F;
    public final static float SPAWN_STASIS_SECONDS = 3F;
    public final static float CLEANUP_FREQUENCY = 3F; // How often in seconds to deallocate creatures out of bounds
    //public static int WATER_LEVEL = 50; //the height of water level in pixel unit
    public static int SHARK_DECTECTION_WIDTH = 38;//attacks if bouncefish fall into this range
    public static int SHARK_DECTECTION_HEIGHT = 1600;
    public static int SHARK_JUMP_HEIGHT = (int)(Screen_Height * 0.42);
    public static int SHARK_ATK_COOLDOWN = 200;
    public static float LEFT_DEALLOCATE_X = -300;
    public static float RIGHT_DEALLOCATE_X = Game_Width + 300;
    public static float BELOW_DEALLOCATE_Y = WATER_LEVEL - 300;
}
