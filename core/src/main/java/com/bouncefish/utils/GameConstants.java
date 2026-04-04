package com.bouncefish.utils;

import com.badlogic.gdx.Gdx;

public final class GameConstants {

    private GameConstants() { } //We never want to instantiate this -- think like C# static class

    // If we want to make any of these editable at runtime, then we'll have to convert this class into something not 'final'
    public static final double GRAVITY = 3000;       // Persistent downward acceleration
    public static final double BOUNCE_DAMPING = 1; // Energy retained on bounce (maybe can make this creature specific?)
    public static final double DEATH_BOUNCE_DAMPING = 0.3; // Purely visual damping multiplier for when the player dies
    public static final double FRICTION = 0.98;      // Friction when bouncing on a creature (maybe can make this creature specific?)
    //public static final double H_ACCEL = 45;        // How fast bouncefish should speed up when inputting left/right (convert this to immediate velocity?)
    public static final double H_SPEED = 800; // Base value for horizontal 'movement' speed
    public static final double MAX_H_SPEED = 700;    // Maximum horizontal speed
    public static final float GROUND_HEIGHT = 0; // Y position of ground level
    public static final int BOUNCEFISH_WIDTH = 200;
    public static final int BOUNCEFISH_HEIGHT = 200;
    public static final int RIGHT_CONTROL_BORDER = 400;
    public static final int LEFT_CONTROL_BORDER = 400;
    public static boolean IS_DEBUG = true;
    public static boolean IS_IMMORTAL = true; // Cheatcode: Use this if you want to be able to bounce on the ground just for testing purposes.

    public static final int MAXIMUM_HARDCODED_WAVES = 2; // Maximum value of waves to do before reaching the endless phase of the game.

    // These are just some wrappers because Gdx.graphics.getWidth feels too long
    public static float Game_Width = Gdx.graphics.getWidth();
    public static float Screen_Height = Gdx.graphics.getHeight();
    public static float MAX_PARALYZED_TIME = 2F;
    public final static float SPAWN_STASIS_SECONDS = 3F;
}
