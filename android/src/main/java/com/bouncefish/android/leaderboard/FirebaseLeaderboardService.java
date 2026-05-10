package com.bouncefish.android.leaderboard;

import com.bouncefish.leaderboard.LeaderboardData;
import com.bouncefish.leaderboard.LeaderboardService;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class FirebaseLeaderboardService implements LeaderboardService {
    FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    public void submitScore(String name, int score) {
        LeaderboardData data = new LeaderboardData(name, score);
        database.collection("leaderboard").add(data);
    }

    @Override
    public void fetchTopScores(Callback callback) {
        database.collection("leaderboard")
            .orderBy("score", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener(queryDocumentSnapshots -> {
                List<LeaderboardData> scores = queryDocumentSnapshots.toObjects(LeaderboardData.class);
                callback.onDataRetrieved(scores);
            })
            .addOnFailureListener(callback::onError);
    }
}
