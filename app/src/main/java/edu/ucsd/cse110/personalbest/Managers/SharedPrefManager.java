package edu.ucsd.cse110.personalbest.Managers;

import android.content.SharedPreferences;

import edu.ucsd.cse110.personalbest.Exercise;
import edu.ucsd.cse110.personalbest.IUserObserver;
import edu.ucsd.cse110.personalbest.User;

import static edu.ucsd.cse110.personalbest.Helpers.StepIntHelper.*;

public class SharedPrefManager implements IUserObserver {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static final String USER_GOAL_KEY = "userGoal";
    public static final String USER_WALK_KEY = "userWalk";
    public static final String USER_EXERCISE_KEY = "userExercise";

    public SharedPrefManager(SharedPreferences sharedPreferences){
        this.sharedPreferences=sharedPreferences;
        this.editor=sharedPreferences.edit();
    }

    @Override
    public void onDataChange(User user) {
        this.publishData(user);
    }

    public void publishData(User user){
        editor.putInt(USER_GOAL_KEY,user.getGoal());
        editor.putInt(USER_EXERCISE_KEY,user.getCurExercise().getStep());
        editor.putInt(USER_WALK_KEY,user.getCurSteps());
        editor.commit();
    }

    public void retrieveData(User user){
        user.setGoal(sharedPreferences.getInt(USER_GOAL_KEY,5000));
        user.setCurSteps(sharedPreferences.getInt(USER_WALK_KEY,0));
        user.setCurExercise(new Exercise(sharedPreferences.getInt(USER_EXERCISE_KEY,0)));
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
