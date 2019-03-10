package edu.ucsd.cse110.personalbest.Managers;

import android.content.SharedPreferences;
import android.util.Log;

import javax.microedition.khronos.egl.EGLDisplay;

import edu.ucsd.cse110.personalbest.Exercise;
import edu.ucsd.cse110.personalbest.IUserObserver;
import edu.ucsd.cse110.personalbest.User;

import static edu.ucsd.cse110.personalbest.Helpers.StepIntHelper.*;

public class SharedPrefManager implements IUserObserver {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    User user;
    public static final String USER_GOAL_KEY = "userGoal";
    public static final String USER_WALK_KEY = "userWalk";
    public static final String USER_EMAIL_KEY = "userEmail";
    public static final String USER_EXERCISE_KEY = "userExercise";

    public SharedPrefManager(SharedPreferences sharedPreferences, User user){
        this.sharedPreferences = sharedPreferences;
        this.editor = sharedPreferences.edit();
        this.user = user;
        this.user.register(this);
    }

    @Override
    public void onDataChange() {
        this.publishData();
    }

    public void publishData(){
        editor.putInt(USER_GOAL_KEY, this.user.getGoal());
        editor.putInt(USER_EXERCISE_KEY, this.user.getCurExercise().getStep());
        editor.putInt(USER_WALK_KEY, this.user.getCurSteps());
        editor.putString(USER_EMAIL_KEY, this.user.getEmailAddress());
        Log.i("SharedPrefManager Set", this.user.getEmailAddress());
        editor.commit();
    }

    public void retrieveData(){
        this.user.setGoal(sharedPreferences.getInt(USER_GOAL_KEY,5000), false);
        this.user.setCurSteps(sharedPreferences.getInt(USER_WALK_KEY,0), false);
        this.user.setCurExercise(new Exercise(sharedPreferences.getInt(USER_EXERCISE_KEY,0)), false);
        this.user.setEmailAddress(sharedPreferences.getString(USER_EMAIL_KEY, ""), false);
        Log.i("SharedPrefManager Retrieve", sharedPreferences.getString(USER_EMAIL_KEY, ""));
        Log.i("SharedPrefManager Retrieve", sharedPreferences.getInt(USER_GOAL_KEY,5000) + "");
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
