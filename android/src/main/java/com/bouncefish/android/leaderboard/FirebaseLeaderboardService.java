package com.bouncefish.android.leaderboard;

import android.util.Log;

import com.bouncefish.leaderboard.LeaderboardData;
import com.bouncefish.leaderboard.LeaderboardService;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FirebaseLeaderboardService implements LeaderboardService {
    FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    public void submitScore(String name, int score) {
        // Document reference is a good way to refer to a location to read and write from
        DocumentReference docRef = database.collection("leaderboard").document(name);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot snapshot) {
                    if (!snapshot.exists() || snapshot.getLong("score") < score) {
                        docRef.set(new LeaderboardData(name, score))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("Leaderboard", "Score updated: " + name + " - " + score);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(Exception e) {
                                    Log.e("Leaderboard", "Submit failed! " + e.getMessage());
                                }
                            });
                    } else {
                        database.collection("leaderboard").add(new LeaderboardData(name, score));
                        Log.d("Leaderboard", "Score not updated! Existing score is higher");
                    }
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(Exception e) {
                    Log.e("Leaderboard", "Failed to fetch leaderboard: " + e.getMessage());
                }
            });
    }

    @Override
    public void fetchTopScores(Callback callback) {
        database.collection("leaderboard")
            .orderBy("score", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List<LeaderboardData> scores = queryDocumentSnapshots.toObjects(LeaderboardData.class);
                    callback.onDataRetrieved(scores);
                }
            })
            .addOnFailureListener(callback::onError);
    }
}
