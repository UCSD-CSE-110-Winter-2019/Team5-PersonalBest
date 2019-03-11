package edu.ucsd.cse110.personalbest.Managers;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.ucsd.cse110.personalbest.IUserObserver;
import edu.ucsd.cse110.personalbest.User;

public class FireStoreManager implements IUserObserver {
    public static final String TAG = "FireStoreManager";

    User user;
    CollectionReference collectionReference;
    DocumentReference documentReference;
    public static final String COLLECTION_KEY = "Users";
    public static final String USER_GOAL_KEY = "Goal";
    public static final String USER_WALK_KEY = "Walk";
    public static final String USER_EXERCISE_KEY = "Exercise";
    public static final String GOAL_HISTORY_KEY = "GoalHistory";
    public static final String WALK_HISTORY_KEY = "WalkHistroy";
    public static final String EXERCISE_HISTORY_KEY = "ExerciseHistory";



    public FireStoreManager(User user){
        this.collectionReference = FirebaseFirestore.getInstance().collection(COLLECTION_KEY);
        this.user = user;
        this.user.register(this);
    }

    @Override
    public void onDataChange() {
        this.uploadData();
    }

    public void uploadData(){
        if (this.user.getEmailAddress().equals("")) {
            this.documentReference = this.collectionReference.document("default");
        } else {
            this.documentReference = this.collectionReference.document(this.user.getEmailAddress());
        }

        Map<String, Integer> goal_info = new HashMap<>();
        goal_info.put(USER_GOAL_KEY, this.user.getGoal());
        this.documentReference.set(goal_info, SetOptions.merge());

        Map<String, Integer> step_info = new HashMap<>();
        step_info.put(USER_WALK_KEY, this.user.getTotalSteps());
        this.documentReference.set(step_info, SetOptions.merge());

        Map<String, Integer> exercise_info = new HashMap<>();
        exercise_info.put(USER_EXERCISE_KEY, this.user.getCurExercise().getStep());
        this.documentReference.set(exercise_info, SetOptions.merge());

        Map<String, ArrayList> goal_history_info = new HashMap<>();
        goal_history_info.put(GOAL_HISTORY_KEY, this.user.getGoalHistory());
        this.documentReference.set(goal_history_info, SetOptions.merge());

        Map<String, ArrayList> walk_history_info = new HashMap<>();
        walk_history_info.put(WALK_HISTORY_KEY, this.user.getWalkHistory());
        this.documentReference.set(walk_history_info, SetOptions.merge());

        Map<String, ArrayList> exercise_history_info = new HashMap<>();
        exercise_history_info.put(EXERCISE_HISTORY_KEY, this.user.getExerciseHistory());
        this.documentReference.set(exercise_history_info, SetOptions.merge());

        /*
        this.documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        */

    }

}
