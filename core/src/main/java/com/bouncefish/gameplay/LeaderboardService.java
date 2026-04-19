package com.bouncefish.gameplay;

import java.util.List;

public interface LeaderboardService {
    void submitScore(String playerName, int score);
    void fetchTopScores(ScoreCallback callback);

    interface ScoreCallback {
        void onScoresLoaded(List<ScoreEntry> scores);
        void onError(Exception e);
    }

    class ScoreEntry {
        public String name;
        public int score;

        public ScoreEntry() {} // Required for Firebase

        public ScoreEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }
    }
}
