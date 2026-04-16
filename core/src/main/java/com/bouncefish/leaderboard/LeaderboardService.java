package com.bouncefish.leaderboard;

import java.util.List;

public interface LeaderboardService {
    void submitScore(String name, int score);
    void fetchTopScores(Callback callback);

    interface Callback {
        void onDataRetrieved(List<LeaderboardData> scores);
        void onError(Exception e);
    }
}
