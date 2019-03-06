package edu.ucsd.cse110.personalbest.Managers;

import android.content.SharedPreferences;

import edu.ucsd.cse110.personalbest.Exercise;
import edu.ucsd.cse110.personalbest.Goal;
import edu.ucsd.cse110.personalbest.ISubject;
import edu.ucsd.cse110.personalbest.IUserObserver;
import edu.ucsd.cse110.personalbest.Step;
import edu.ucsd.cse110.personalbest.User;
import edu.ucsd.cse110.personalbest.Walk;

import static edu.ucsd.cse110.personalbest.Helpers.StepIntHelper.*;

public class SharedPrefManager implements IUserObserver {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String USER_GOAL_KEY = "userGoal";
    public static final String USER_GOAL_HISTORY_KEY = "userGoalHistory";
    public static final String USER_HEIGHT_KEY = "userHeight";
    public static final String USER_WALK_KEY = "userWalk";
    public static final String USER_WALK_HISTORY_KEY = "userWalkHistory";
    public static final String USER_EXERCISE_KEY = "userExercise";
    public static final String USER_EXERCISE_HISTORY_KEY = "userExerciseHistory";

    public SharedPrefManager(SharedPreferences sharedPreferences){
        this.sharedPreferences=sharedPreferences;
        this.editor=sharedPreferences.edit();
    }

    @Override
    public void onDataChange(User user) {
        this.publishData(user);
    }

    public void publishData(User user){
        editor.putInt(USER_GOAL_KEY,user.getGoal().getStep());
        editor.putInt(USER_EXERCISE_KEY,user.getCurrentExercise().getStep());
        editor.putInt(USER_WALK_KEY,user.getCurrentWalk().getStep());
        editor.putInt(USER_HEIGHT_KEY,user.getHeight());
        storeIntArray(USER_GOAL_HISTORY_KEY,extractStepArray(user.getGoalHistory()));
        storeIntArray(USER_EXERCISE_HISTORY_KEY,extractStepArray(user.getExerciseHistory()));
        storeIntArray(USER_WALK_HISTORY_KEY,extractStepArray(user.getWalkHistory()));
        editor.commit();
    }

    public void retrieveData(User user){
        user.setGoal(new Goal(sharedPreferences.getInt(USER_GOAL_KEY,5000)));
        user.setGoalHistory(constructGoalArray(parseIntArray(USER_GOAL_HISTORY_KEY)));
        user.setCurrentWalk(new Walk(sharedPreferences.getInt(USER_WALK_KEY,0)));
        user.setWalkHistory(constructWalkArray(parseIntArray(USER_WALK_HISTORY_KEY)));
        user.setCurrentExercise(new Exercise(sharedPreferences.getInt(USER_EXERCISE_KEY,0)));
        user.setExerciseHistory(constructExerciseArray(parseIntArray(USER_EXERCISE_HISTORY_KEY)));
        user.setHeight(sharedPreferences.getInt(USER_HEIGHT_KEY,180));
    }

    private void storeIntArray(String baseKey,int[] intArray){
        for(int i=0;i<intArray.length;i++){
            editor.putInt(baseKey+i,intArray[i]);
        }
    }

    private int[] parseIntArray(String baseKey){
        int[] retArray=new int[7];
        for(int i=0;i<retArray.length;i++){
            retArray[i]=sharedPreferences.getInt(baseKey+i,0);
        }
        return retArray;
    }
}
