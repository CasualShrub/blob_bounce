package com.bouncefish.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.bouncefish.Main;
import com.bouncefish.android.leaderboard.FirebaseLeaderboardService;
import com.bouncefish.leaderboard.LeaderboardService;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useImmersiveMode = true; // Recommended, but not required.
        LeaderboardService service = new FirebaseLeaderboardService();
        initialize(new Main(service), configuration);
    }
}
