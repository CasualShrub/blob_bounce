package com.bouncefish.android;

import android.util.Log;
import com.bouncefish.gameplay.LeaderboardService;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FirebaseLeaderboard implements LeaderboardService {
    private DatabaseReference database;
    private static final String TAG = "FirebaseLeaderboard";

    public FirebaseLeaderboard() {
        database = FirebaseDatabase.getInstance().getReference("leaderboard");
        Log.d(TAG, "Firebase Database Initialized");
    }

    @Override
    public void submitScore(String playerName, int score) {
        Log.d(TAG, "Attempting to submit score: " + score + " for " + playerName);
        String key = database.push().getKey();
        if (key != null) {
            database.child(key).setValue(new ScoreEntry(playerName, score))
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Score submitted successfully!"))
                .addOnFailureListener(e -> Log.e(TAG, "Failed to submit score", e));
        }
    }

    @Override
    public void fetchTopScores(final ScoreCallback callback) {
        database.orderByChild("score").limitToLast(10)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<ScoreEntry> scores = new ArrayList<>();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ScoreEntry entry = snapshot.getValue(ScoreEntry.class);
                        if (entry != null) {
                            scores.add(entry);
                        }
                    }
                    Collections.reverse(scores);
                    callback.onScoresLoaded(scores);
                    Log.d(TAG, "Scores fetched: " + scores.size());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "Database Error: " + databaseError.getMessage());
                    callback.onError(databaseError.toException());
                }
            });
    }
}
