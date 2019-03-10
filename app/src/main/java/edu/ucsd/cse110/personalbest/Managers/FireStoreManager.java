package edu.ucsd.cse110.personalbest.Managers;

import android.content.SharedPreferences;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLDisplay;

import edu.ucsd.cse110.personalbest.Exercise;
import edu.ucsd.cse110.personalbest.IUserObserver;
import edu.ucsd.cse110.personalbest.User;

public class FireStoreManager implements IUserObserver {
    User user;
    CollectionReference collectionReference;
    DocumentReference documentReference;
    public static final String COLLECTION_KEY = "users";
    public static final String USER_GOAL_KEY = "userGoal";
    public static final String USER_WALK_KEY = "userWalk";
    public static final String USER_EXERCISE_KEY = "userExercise";


    public FireStoreManager(User user){
        this.user = user;
        this.user.register(this);
        collectionReference = FirebaseFirestore.getInstance().collection(COLLECTION_KEY);
        documentReference = collectionReference.document("abc@ucsd.edu");
    }

    @Override
    public void onDataChange() {
        this.publishData();
    }

    public void publishData(){
        Map<String, Integer> goal_info = new HashMap<>();
        goal_info.put(USER_GOAL_KEY, this.user.getGoal());
        this.documentReference.set(goal_info, SetOptions.merge());

        Map<String, Integer> step_info = new HashMap<>();
        step_info.put(USER_WALK_KEY, this.user.getCurSteps());
        this.documentReference.set(step_info, SetOptions.merge());

        Map<String, Integer> exercise_info = new HashMap<>();
        exercise_info.put(USER_EXERCISE_KEY, this.user.getCurExercise().getStep());
        this.documentReference.set(exercise_info, SetOptions.merge());
        
    }

    public void retrieveData(){

    }
}
