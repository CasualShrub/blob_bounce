package com.bouncefish.leaderboard;

public class LeaderboardData {
    public String name;
    public int score;

    public LeaderboardData() {} // Required for Firebase

    public LeaderboardData(String name, int score) {
        this.name = name;
        this.score = score;
    }
}
