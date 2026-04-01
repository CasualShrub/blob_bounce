package com.bouncefish.gameplay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

import java.util.Random;

// This class will be responsible for playing
public class SoundManager {

    private static Random rand;
    private static final Sound[] bounceSounds = {Gdx.audio.newSound(Gdx.files.internal("sfx/bounce1.mp3")),
                                        Gdx.audio.newSound(Gdx.files.internal("sfx/bounce2.mp3"))};

    public SoundManager(){
        rand = new Random();
    }

    public static void playBounceSound(){
        int i = rand.nextInt(bounceSounds.length);
        bounceSounds[i].play();
    }

    public void disposeSounds(){
        for (Sound sound: bounceSounds) {
            sound.dispose();
        }
    }
}
