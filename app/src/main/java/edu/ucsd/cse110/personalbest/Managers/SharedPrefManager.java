package edu.ucsd.cse110.personalbest.Managers;

import android.content.SharedPreferences;
import android.util.Log;

import edu.ucsd.cse110.personalbest.Exercise;
import edu.ucsd.cse110.personalbest.IUserObserver;
import edu.ucsd.cse110.personalbest.User;

public class SharedPrefManager implements IUserObserver {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    User user;
    public static final String USER_EMAIL_KEY = "Email";
    public static final String DAY_KEY = "curDay";
    public static final String USER_GOAL_KEY = "Goal";
    public static final String USER_WALK_KEY = "Walk";
    public static final String USER_EXERCISE_KEY = "Exercise";
    public static final String GOAL_HISTORY_KEY = "GoalHistory";
    public static final String WALK_HISTORY_KEY = "WalkHistroy";
    public static final String EXERCISE_HISTORY_KEY = "ExerciseHistory";
    public static final String SIZE_KEY = "Size";

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
        editor.putInt(USER_WALK_KEY, this.user.getTotalSteps());
        editor.putString(USER_EMAIL_KEY, this.user.getEmailAddress());
        editor.putInt(DAY_KEY, this.user.getCurrentDay());

        int size = this.user.getGoalHistory().size();
        editor.putInt(SIZE_KEY, size);
        for (int i = 0; i < size; i++) {
            editor.putInt(GOAL_HISTORY_KEY + i, (int)this.user.getGoalHistory().get(i));
            editor.putInt(WALK_HISTORY_KEY + i, (int)this.user.getWalkHistory().get(i));
            editor.putInt(EXERCISE_HISTORY_KEY + i, (int)this.user.getExerciseHistory().get(i));
        }

        Log.i("SharedPrefManager Set Goal", this.user.getGoal() + "");
        Log.i("SharedPrefManager Set Exer", this.user.getCurExercise().getStep() + "");
        Log.i("SharedPrefManager Set Walk", this.user.getTotalSteps() + "");
        Log.i("SharedPrefManager Set CurDay", this.user.getCurrentDay() + "");
        Log.i("SharedPrefManager Set Email", this.user.getEmailAddress() + "");
        Log.i("SharedPrefManager Set Size", this.user.getGoalHistory().size() + "");
        editor.commit();
    }

    public void retrieveData(){
        this.user.setGoal(sharedPreferences.getInt(USER_GOAL_KEY,5000), false);
        this.user.setTotalSteps(sharedPreferences.getInt(USER_WALK_KEY,0), false);
        this.user.setCurExercise(new Exercise(sharedPreferences.getInt(USER_EXERCISE_KEY,0)), false);
        this.user.setEmailAddress(sharedPreferences.getString(USER_EMAIL_KEY, ""), false);

        int size = sharedPreferences.getInt(SIZE_KEY, 0);
        for (int i = 0; i < size; i++) {
            this.user.getGoalHistory().add(sharedPreferences.getInt(GOAL_HISTORY_KEY + i,0));
            this.user.getWalkHistory().add(sharedPreferences.getInt(WALK_HISTORY_KEY + i,0));
            this.user.getExerciseHistory().add(sharedPreferences.getInt(EXERCISE_HISTORY_KEY + i,0));
        }
        if (size == 0) {
            this.user.getGoalHistory().add(5000);
            this.user.getWalkHistory().add(0);
            this.user.getExerciseHistory().add(0);
        }

        // Must be the last one
        this.user.compareCurrentDay(sharedPreferences.getInt(DAY_KEY, Integer.MAX_VALUE));

        Log.i("SharedPrefManager Retrieve Goal", sharedPreferences.getInt(USER_GOAL_KEY,5000)+ "");
        Log.i("SharedPrefManager Retrieve Walk", sharedPreferences.getInt(USER_WALK_KEY,0) + "");
        Log.i("SharedPrefManager Retrieve Exer", sharedPreferences.getInt(USER_EXERCISE_KEY,0) + "");
        Log.i("SharedPrefManager Retrieve Email", sharedPreferences.getString(USER_EMAIL_KEY, "") + "");
        Log.i("SharedPrefManager Retrieve Size", sharedPreferences.getInt(SIZE_KEY, 0) + "");
        Log.i("SharedPrefManager Retrieve Curday", sharedPreferences.getInt(DAY_KEY,0) + "");
    }
}
