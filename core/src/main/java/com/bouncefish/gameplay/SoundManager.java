package com.bouncefish.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import java.util.Random;

public class SoundManager {

    private static Random rand = new Random();
    private static Sound[] bounceSounds;
    private static boolean isInitialized = false;

    public SoundManager() {
        if (!isInitialized) {
            init();
        }
    }

    private static void init() {
        try {
            bounceSounds = new Sound[]{
                Gdx.audio.newSound(Gdx.files.internal("sfx/bounce1.mp3")),
                Gdx.audio.newSound(Gdx.files.internal("sfx/bounce2.mp3"))
            };
            isInitialized = true;
        } catch (Exception e) {
            System.err.println("Could not load sounds: " + e.getMessage());
        }
    }

    public static void playBounceSound() {
        if (!isInitialized) init();

        if (bounceSounds != null && bounceSounds.length > 0) {
            int i = rand.nextInt(bounceSounds.length);
            bounceSounds[i].play(0.5f); // Play at 50% volume for a "light" effect
        }
    }

    public void disposeSounds() {
        if (bounceSounds != null) {
            for (Sound sound : bounceSounds) {
                sound.dispose();
            }
        }
        isInitialized = false;
    }
}
