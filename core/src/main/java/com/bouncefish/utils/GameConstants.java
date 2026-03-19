package com.bouncefish.utils;

public final class GameConstants {

    private GameConstants() { } //We never want to instantiate this -- think like C# static class

    // If we want to make any of these editable at runtime, then we'll have to convert this class into something not 'final'
    public static final double GRAVITY = 75.2;       // Persistent downward acceleration
    public static final double BOUNCE_DAMPING = 1; // Energy retained on bounce (maybe can make this creature specific?)
    public static final double FRICTION = 0.98;      // Friction when bouncing on a creature (maybe can make this creature specific?)
    public static final double H_ACCEL = 45;        // How fast bouncefish should speed up when inputting left/right (convert this to immediate velocity?)
    public static final double MAX_H_SPEED = 8.0;    // Maximum horizontal speed
    public static final double GROUND_HEIGHT = 0; // Y position of ground level
}
